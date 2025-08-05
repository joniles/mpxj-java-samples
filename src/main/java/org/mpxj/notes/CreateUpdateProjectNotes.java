package org.mpxj.notes;

import org.mpxj.reader.UniversalProjectReader;
import org.mpxj.writer.FileFormat;
import org.mpxj.writer.UniversalProjectWriter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mpxj.Notes;
import org.mpxj.NotesTopic;
import org.mpxj.ParentNotes;
import org.mpxj.ProjectFile;
import org.mpxj.StructuredNotes;

/**
 * Reads an XER or PMXML file and adds or updates project notes using custom topic.
 */
public class CreateUpdateProjectNotes
{
   public static void main(String[] argv) throws Exception
   {
      if (argv.length != 2)
      {
         System.out.println("UpdateProjectNotes <input file> <output file>");
         return;
      }

      new CreateUpdateProjectNotes().process(argv[0], argv[1]);
   }

   public void process(String inputFile, String outputFile) throws Exception
   {
      // Read our file
      ProjectFile file = new UniversalProjectReader().read(inputFile);

      // Make sure we have a PMXML or XER file
      String fileType = file.getProjectProperties().getFileType();
      if (!SUPPORTED_FILE_TYPES.contains(fileType))
      {
         throw new IllegalArgumentException("Unsupported file type: " + fileType);
      }

      FileFormat fileFormat =  fileType.equals("PMXML") ? FileFormat.PMXML : FileFormat.XER;

      // Find or create the notes topic we want to use
      NotesTopic topic = file.getNotesTopics().stream()
         .filter(t -> MPXJ_TOPIC_NAME.equals(t.getName()))
         .findFirst().orElseGet(() -> createNotesTopic(file));

      // Find or create the parent notes entry
      ParentNotes parentNotes;
      if (fileFormat == FileFormat.PMXML)
      {
         parentNotes = (ParentNotes)file.getProjectProperties().getNotesObject();
         if (parentNotes == null)
         {
            parentNotes = new ParentNotes(new ArrayList<>());
            file.getProjectProperties().setNotesObject(parentNotes);
         }
      }
      else
      {
         parentNotes = (ParentNotes)file.getTasks().get(0).getNotesObject();
         if (parentNotes == null)
         {
            parentNotes = new ParentNotes(new ArrayList<>());
            file.getTasks().get(0).setNotesObject(parentNotes);
         }
      }

      // Add or update the notes for our topic
      /// NOTE: we are only working with plain text here and stripping any HTML formatting
      List<Notes> childNotes = parentNotes.getChildNotes();
      StructuredNotes notes = childNotes.stream().map(n -> (StructuredNotes)n).filter(n -> n.getNotesTopic() == topic).findFirst().orElse(null);
      if (notes == null)
      {
         childNotes.add(new StructuredNotes(file, null, topic, new Notes("Created: " + LocalDateTime.now())));
      }
      else
      {
         String existingText = notes.getNotes().toString();
         String newText = existingText + "\n" + "Updated: " + LocalDateTime.now();
         childNotes.remove(notes);
         childNotes.add(new StructuredNotes(file, null, topic, new Notes(newText)));
      }

      new UniversalProjectWriter(fileFormat).write(file, outputFile);
   }

   private NotesTopic createNotesTopic(ProjectFile file)
   {
      NotesTopic topic = new NotesTopic.Builder(file)
         .name(MPXJ_TOPIC_NAME)
         .availableForProject(true).build();
      file.getNotesTopics().add(topic);
      return topic;
   }

   private final String MPXJ_TOPIC_NAME = "MPXJ Topic";
   private static final Set<String> SUPPORTED_FILE_TYPES = Stream.of("PMXML", "XER").collect(Collectors.toSet());
}
