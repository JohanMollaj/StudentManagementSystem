package util;

import javax.swing.JOptionPane;

/**
 * Klasa DialogHelper - ndihmon me shfaqjen e mesazheve.
 * Në vend të shkruajmë JOptionPane çdo herë,
 * thjesht thërrasim metodat e kësaj klase.
 *
 * Shembull përdorimi:
 *   DialogHelper.showSuccess("Studenti u shtua me sukses!");
 *   DialogHelper.showError("Email-i nuk është i vlefshëm!");
 */
public class DialogHelper {

    /**
     * Tregon mesazh suksesi (ngjyrë jeshile, ikonë ✓).
     *
     * @param msg  teksti i mesazhit
     */
    public static void showSuccess(String msg) {
        JOptionPane.showMessageDialog(
                null,           // prind null = shfaqet në qendër të ekranit
                msg,            // teksti i mesazhit
                "Sukses ✓",    // titulli i dritares
                JOptionPane.INFORMATION_MESSAGE  // ikonë informacioni (i blujë)
        );
    }

    /**
     * Tregon mesazh gabimi (ikonë ✗ e kuqe).
     *
     * @param msg  teksti i gabimit
     */
    public static void showError(String msg) {
        JOptionPane.showMessageDialog(
                null,
                msg,
                "Gabim ✗",
                JOptionPane.ERROR_MESSAGE  // ikonë gabimi (e kuqe)
        );
    }

    /**
     * Tregon mesazh paralajmërimi (ikonë ⚠ e verdhë).
     *
     * @param msg  teksti i paralajmërimit
     */
    public static void showWarning(String msg) {
        JOptionPane.showMessageDialog(
                null,
                msg,
                "Paralajmërim ⚠",
                JOptionPane.WARNING_MESSAGE  // ikonë paralajmërimi (e verdhë)
        );
    }

    /**
     * Tregon dialog konfirmimi me dy butona: Po dhe Jo.
     * Përdoret para fshirjes së të dhënave.
     *
     * @param msg  pyetja e konfirmimit
     * @return true nëse përdoruesi klikon "Po"; false nëse klikon "Jo"
     */
    public static boolean showConfirm(String msg) {
        // showConfirmDialog kthen një numër:
        // JOptionPane.YES_OPTION = 0  → Poshtë
        // JOptionPane.NO_OPTION  = 1  → Jo
        int result = JOptionPane.showConfirmDialog(
                null,
                msg,
                "Konfirmo Veprimin",
                JOptionPane.YES_NO_OPTION,     // dy butona: Po / Jo
                JOptionPane.QUESTION_MESSAGE   // ikonë pyetje
        );
        // Kthejmë true vetëm nëse klikoi "Po"
        return result == JOptionPane.YES_OPTION;
    }
}