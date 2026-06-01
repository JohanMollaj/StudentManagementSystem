import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileBackup {

    public static void exportToCSV(ArrayList<Student> list) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ruaj CSV");
        fileChooser.setSelectedFile(new File("students_backup.csv"));

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                // header
                bw.write("student_id,first_name,last_name,email,birth_date,gender,status");
                bw.newLine();
                // te dhenat
                for (Student s : list) {
                    bw.write(String.join(",",
                            s.getStudentId(),
                            s.getFirstName(),
                            s.getLastName(),
                            s.getEmail(),
                            s.getBirthDate() != null ? s.getBirthDate().toString() : "",
                            s.getGender() != null ? s.getGender() : "",
                            s.getStatus()
                    ));
                    bw.newLine();
                }
                JOptionPane.showMessageDialog(null, "✓ Eksporti u krye me sukses!\n" + file.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Gabim gjate eksportit: " + e.getMessage(),
                        "Gabim", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static ArrayList<Student> importFromCSV() {
        ArrayList<Student> list = new ArrayList<>();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Hap CSV");

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                br.readLine(); // kalo headerin
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        LocalDate bd = parts[4].isEmpty() ? null : LocalDate.parse(parts[4]);
                        Student s = new Student(parts[0], parts[1], parts[2], parts[3], bd, parts[6]);
                        s.setGender(parts[5]);
                        list.add(s);
                    }
                }
                JOptionPane.showMessageDialog(null, "✓ U importuan " + list.size() + " studente!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Gabim gjate importit: " + e.getMessage(),
                        "Gabim", JOptionPane.ERROR_MESSAGE);
            }
        }
        return list;
    }
}