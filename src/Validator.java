package util;

import javax.swing.JOptionPane;

/**
 * Klasa Validator - kontrollon nëse të dhënat janë të sakta.
 * Çdo metodë kontrollon një gjë të ndryshme dhe
 * tregon mesazh gabimi nëse diçka nuk shkon.
 *
 * Shembull përdorimi:
 *   if (!Validator.isValidEmail("test")) { ... }  // do të tregojë gabim
 */
public class Validator {

    /**
     * Kontrollon nëse një fushë teksti është bosh ose ka vetëm hapësira.
     *
     * @param value  vlera që duam të kontrollojmë
     * @param fieldName  emri i fushës (për mesazhin e gabimit, p.sh. "Emri")
     * @return true nëse fusha ka vlerë; false nëse është bosh
     */
    public static boolean isEmptyField(String value, String fieldName) {
        // trim() heq hapësirat para dhe pas tekstit
        if (value == null || value.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Fusha '" + fieldName + "' nuk mund të jetë bosh!",
                    "Gabim Validimi",
                    JOptionPane.ERROR_MESSAGE);
            return false; // fusha është bosh → jo e vlefshme
        }
        return true; // fusha ka vlerë → e vlefshme
    }

    /**
     * Kontrollon nëse email-i ka formatin e saktë.
     * Formati i saktë: diçka@diçka.diçka  (p.sh. ana@gmail.com)
     *
     * @param email  email-i që duam të kontrollojmë
     * @return true nëse email-i është i vlefshëm; false nëse jo
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Email-i nuk mund të jetë bosh!",
                    "Gabim Validimi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // regex (shprehje e rregullt) për të kontrolluar formatin e email-it
        // [^@]+ = karaktere para @
        // @     = duhet të ketë @
        // [^@]+ = karaktere pas @
        // \.    = duhet të ketë pikë
        // .+    = karaktere pas pikës (p.sh. "com", "al")
        String emailPattern = "[^@]+@[^@]+\\.[^@]+";
        if (!email.matches(emailPattern)) {
            JOptionPane.showMessageDialog(null,
                    "Email-i '" + email + "' nuk ka formatin e saktë!\nShembull i saktë: studenti@uni.edu.al",
                    "Gabim Validimi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Kontrollon nëse ID-ja e studentit është e vlefshme.
     * ID-ja nuk mund të jetë bosh.
     * (Kontrolli i unikalitetit bëhet në StudentManager.)
     *
     * @param id  ID-ja që duam të kontrollojmë
     * @return true nëse ID-ja është e vlefshme; false nëse jo
     */
    public static boolean isValidId(String id) {
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "ID-ja e studentit nuk mund të jetë bosh!",
                    "Gabim Validimi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Kontrollon nëse një vlerë është numër pozitiv.
     * Përdoret p.sh. për nota ose kredite.
     *
     * @param value  vlera si tekst (p.sh. "85")
     * @param fieldName  emri i fushës për mesazhin e gabimit
     * @return true nëse është numër pozitiv; false nëse jo
     */
    public static boolean isPositiveNumber(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Fusha '" + fieldName + "' nuk mund të jetë bosh!",
                    "Gabim Validimi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            // Provojmë ta shndërrojmë në numër
            double number = Double.parseDouble(value.trim());
            if (number < 0) {
                JOptionPane.showMessageDialog(null,
                        "Fusha '" + fieldName + "' duhet të jetë numër pozitiv!",
                        "Gabim Validimi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            // NumberFormatException ndodh kur teksti nuk është numër
            JOptionPane.showMessageDialog(null,
                    "Fusha '" + fieldName + "' duhet të jetë numër (p.sh. 85 ose 9.5)!",
                    "Gabim Validimi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}