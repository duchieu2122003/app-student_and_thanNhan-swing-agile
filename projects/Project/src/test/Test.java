/*
f  */
package test;

import javax.swing.UnsupportedLookAndFeelException;

import view.FromDangNhap;

/**
 *
 * @author dinhv
 */
public class Test {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    new FromDangNhap();
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
        }
    }
}
