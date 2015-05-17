
import java.awt.*;
import java.awt.event.*;
import java.math.RoundingMode;
import java.util.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import org.freixas.jcalendar.*;

public class Main extends JFrame {

    private static final long serialVersionUID = 1L;
    private Frame fr; /*fereastra principal*/
    Frame viz; //ferestreaza adaugare; viz fereastra vizualizare
    private JFrame frm, more; //
    private JMenuBar mb;//bara meniu
    private JMenu m;//panou meniu
    private JMenuItem mi;//panou submeniu
    private Tools t;
    private JTable jtab; /*tabelul*/
    JPopupMenu pop;
    JMenuItem item;
    private JScrollPane sp;// panou intern al ferestrei
    private JLabel lblnume, lblprenume, lblcnp, lblcota, lbldatain, lbldataout, lblsuma, lblpersoana, lblvalcalc, lblcr, lblzile,
            slblnume, slblvalcalc, slblcota, slblsumai, slbldatain, slbldataout, lblsumavalcalc;
    private JLabel lblsumadb;
    private JLabel lbldataplata, lblsumaachitata, lbldebite, lblsold;
    private ComboItem it;
    private JTextField temp, txtnume, txtprenume, txtsuma, txtcota, txtdatain, txtdataout, txtcnp, txtpersoana, txtvalcalc, stxtnume, stxtvalcalc, stxtcota, stxtsumai, stxtdatain, stxtdataout;

    //more..
    private JLabel mlbldatain, mlbldataout, mlblvalcalc, mlblvalfinala, mlblcota, sold;
    private JTextField mtxtdatain, mtxtdataout, mtxtvalcalc, mtxtvalfinala, txtdataplata, txtsumaachitata;

    private JCalendar cal;
    private MyDateListener listener;
    private JComboBox compersoana, comdebite; JComboBox<String> comcota, mcomcota; //ComboBoxModel model;
    JButton btnarr;
    private JButton btnnext, btnback, btnsaven, btnsaver, btndel, btnres, btnclose, btnvalcalc, btnmore, mbtnvalcalc, mbtncalcfinal;
    private int currentrow = 1;
    DefaultTableModel tab;
    String[] str;

    public void Refresh() {

        //if(compersoana == null) return;

        compersoana.removeAllItems();
        comcota.removeAllItems();
        mcomcota.removeAllItems();
        int x = t.CountPersoane();
        //System.out.println(x);

        for (int j = 0; j < x; j++) {
            //compersoana.addItem(t.GetP()[j]);
            //System.out.println(t.GetP()[j]);
            //System.out.println(t.GetP()[j].split(Pattern.quote("|"))[1]);
            str = t.GetP();
            //System.out.println(s);
            compersoana.addItem(new ComboItem(Integer.parseInt(str[j].split(Pattern.quote("|"))[0]), str[j].split(Pattern.quote("|"))[1]));
        }

        int y = t.CountCote();
        for (int k = 0; k < y; k++) {
            comcota.addItem(t.GetC()[k]);
            mcomcota.addItem(t.GetC()[k]);
        }

        //sold.setText(Float.toString(t.GetSold(comdebite.getSelectedItem().toString())));

        //selectare persoana a platii curente
        if (t.CountPlati() > 0)
            compersoana.setSelectedIndex(t.GetPersPlata(currentrow));//curent row este din selectia din plati pt. IdPers selectat respectiv numele din combo
    }


    public Main() {

        fr = new Frame(); //fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //fr.settitle("Calcul Debite");
        //fr.setsize(900, 700);
        fr.setsize(900, 500);
        fr.setlayout();
        fr.setlocationRelativeTo(null); //centrare
        //
        //comune
        viz = new Frame();
        viz.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        viz.setsize(400, 300);
        viz.setlocationRelativeTo(null); //centrare

        //
        frm = new JFrame();
        //frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        frm.setLocationRelativeTo(null); //centrare
        frm.setLayout(null);

        //construim controalele o singura data
        stxtdatain = new JTextField(); //stxtdatain.setName("din");
        stxtdataout = new JTextField(); //stxtdataout.setName("dout");
        //calendar
        cal = new JCalendar(JCalendar.DISPLAY_DATE, true);
        cal.setDayFont(new Font("Arial", Font.PLAIN, 8));

        //model = new DefaultComboBoxModel();

        lblnume = new JLabel();
        lblprenume = new JLabel();
        lblcnp = new JLabel();
        lblcota = new JLabel();
        lbldatain = new JLabel();
        lbldatain.setText("DataIn");
        lbldataout = new JLabel();
        lbldataout.setText("DataOut");
        lblsuma = new JLabel();
        lblsuma.setText("Suma");
        lblpersoana = new JLabel();
        lblpersoana.setText("Persoana");
        lblvalcalc = new JLabel();
        lblvalcalc.setText("ValCalc");
        lblcr = new JLabel();// eticheta currentrow
        lblzile = new JLabel(); //eticheta numar zile
        lblsumavalcalc = new JLabel();

        lblsumadb = new JLabel();

        lbldataplata = new JLabel();
        txtdataplata = new JTextField();
        lblsumaachitata = new JLabel();
        txtsumaachitata = new JTextField();
        lbldebite = new JLabel();

        lblsold = new JLabel();
        sold = new JLabel();

        txtnume = new JTextField();
        txtprenume = new JTextField();
        txtcnp = new JTextField();
        txtcota = new JTextField();
        txtdatain = new JTextField();
        txtdataout = new JTextField();
        txtsuma = new JTextField();
        txtpersoana = new JTextField();
        txtvalcalc = new JTextField();
        btnvalcalc = new JButton();
        btnvalcalc.setText("Calcul");
        compersoana = new JComboBox();
        comcota = new JComboBox<String>();
        comdebite = new JComboBox(); //comdebite.setName("comdebite");
        btnarr = new JButton();
        btnnext = new JButton();
        btnback = new JButton();
        btnsaven = new JButton();
        btnsaver = new JButton();
        btndel = new JButton();
        btnres = new JButton();
        btnclose = new JButton();
        btnmore = new JButton();
        frm.add(lblnume);
        frm.add(lblprenume);
        frm.add(lblcnp);
        frm.add(lblcota);
        frm.add(lbldatain);
        frm.add(lbldataout);
        frm.add(lblsuma);
        frm.add(lblpersoana);
        frm.add(lblvalcalc);
        frm.add(lblcr);
        frm.add(lblzile);
        frm.add(txtnume);
        frm.add(txtprenume);
        frm.add(txtcnp);
        frm.add(txtcota);
        frm.add(txtdatain);
        frm.add(txtdataout);
        frm.add(txtsuma);
        frm.add(txtpersoana);
        frm.add(txtvalcalc);
        frm.add(compersoana);
        frm.add(comcota);
        frm.add(btnnext);
        frm.add(btnback);
        frm.add(btnsaven);
        frm.add(btnsaver);
        frm.add(btndel);
        frm.add(btnres);
        frm.add(btnclose);
        frm.add(btnvalcalc);
        frm.add(btnmore);

        frm.add(lbldataplata);
        frm.add(txtdataplata);
        frm.add(lblsumaachitata);
        frm.add(txtsumaachitata);
        frm.add(lbldebite);
        frm.add(comdebite);
        frm.add(lblsold);
        frm.add(sold);

        lblnume.setText("Nume:");
        lblprenume.setText("Prenume:");
        lblcnp.setText("CNP:");
        lblcota.setText("Cota:");
        btnnext.setText("Next");
        btnback.setText("Back");
        btnsaven.setText("Save N");
        lbldebite.setText("Debite:");
        lblsold.setText("Sold:");
        btnsaver.setText("Save R");
        btndel.setText("Delete");
        btnres.setText("Reset");
        btnclose.setText("Close");
        btnmore.setText("More");

        lbldataplata.setText("Data platii");
        lblsumaachitata.setText("Suma platita");

        //controale fereastra principala
        slblnume = new JLabel();
        slblvalcalc = new JLabel();
        slblcota = new JLabel();
        slblsumai = new JLabel();
        slbldatain = new JLabel();
        slbldataout = new JLabel();

        slblnume.setText("Numele");
        slblvalcalc.setText("Val calc");
        slblcota.setText("Cota");
        slblsumai.setText("SumaI");
        slbldatain.setText("Data inc.");
        slbldataout.setText("Data sf.");

        slblnume.setBounds(12, 5, 82, 23);
        slblvalcalc.setBounds(103, 5, 82, 23);
        slblcota.setBounds(194, 5, 82, 23);
        slblsumai.setBounds(286, 5, 82, 23);
        slbldatain.setBounds(378, 5, 82, 23);
        slbldataout.setBounds(470, 5, 82, 23);

        fr.addd(slblnume);
        fr.addd(slblvalcalc);
        fr.addd(slblcota);
        fr.addd(slblsumai);
        fr.addd(slbldatain);
        fr.addd(slbldataout);

        stxtnume = new JTextField();
        stxtvalcalc = new JTextField();
        stxtcota = new JTextField();
        stxtsumai = new JTextField();
        stxtdatain = new JTextField();
        stxtdataout = new JTextField(); //stxtdataout.setText("3000-01-01");

        stxtnume.setBounds(10, 35, 82, 23);
        stxtvalcalc.setBounds(100, 35, 82, 23);
        stxtcota.setBounds(190, 35, 82, 23);
        stxtsumai.setBounds(280, 35, 82, 23);
        stxtdatain.setBounds(370, 35, 75, 23);
        cal.setBounds(570, 15, 300, 300);
        stxtdataout.setBounds(470, 35, 75, 23);

        fr.addd(stxtnume);
        fr.addd(stxtvalcalc);
        fr.addd(stxtcota);
        fr.addd(stxtsumai);
        fr.addd(stxtdatain);
        fr.addd(cal);
        fr.addd(stxtdataout);
        fr.addd(lblsumavalcalc);
        lblsumavalcalc.setBounds(75, 380, 100, 25);
        fr.addd(lblsumadb);
        lblsumadb.setBounds(270, 380, 120, 25); //lblsumadb.setText("Db");
        Tools.lblsumavalcalc = lblsumavalcalc;
        Tools.lblsumadb = lblsumadb;

        tab = new DefaultTableModel();

        temp = new JTextField();
        pop = new JPopupMenu();
        pop.add(item = new JMenuItem("Reset"));
        // pop.add(item = new JMenuItem("Test"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                temp.setText("");
                //stxtnume.setText(""); stxtvalcalc.setText(""); stxtcota.setText(""); stxtsumai.setText("");
                //stxtdatain.setText(""); stxtdataout.setText("");
            }
        });


        //more...
        more = new JFrame();
        //more.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        more.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        more.setLocationRelativeTo(null); //centrare
        more.setLayout(null);
        more.setTitle("More...");
        more.setSize(230, 250);

        //private JComboBox<String> mcomcota;
        mlbldatain = new JLabel();
        mlbldataout = new JLabel();
        mlblvalcalc = new JLabel();
        mlblvalfinala = new JLabel();
        mlblcota = new JLabel();
        mtxtdatain = new JTextField();
        mtxtdataout = new JTextField();
        mtxtvalcalc = new JTextField();
        mtxtvalfinala = new JTextField();
        mcomcota = new JComboBox<String>();
        mbtnvalcalc = new JButton();
        mbtncalcfinal = new JButton();

        more.add(mlbldatain);
        more.add(mlbldataout);
        more.add(mlblvalcalc);
        more.add(mlblvalfinala);
        more.add(mlblcota);
        more.add(mtxtdatain);
        more.add(mtxtdatain);
        more.add(mtxtdataout);
        more.add(mtxtvalcalc);
        more.add(mtxtvalfinala);
        more.add(mcomcota);
        more.add(mbtnvalcalc);
        more.add(mbtncalcfinal);

        mlbldatain.setText("Data in:");
        mlbldataout.setText("Data out:");
        mlblvalcalc.setText("Val. calc.:");
        mlblvalfinala.setText("Val. finala:");
        mlblcota.setText("Cota:");
        //pozitii
        mlbldatain.setBounds(20, 15, 80, 23);
        mlbldataout.setBounds(20, 40, 80, 23);
        mlblvalcalc.setBounds(20, 65, 82, 23);
        mlblvalfinala.setBounds(20, 90, 82, 23);
        mlblcota.setBounds(20, 115, 82, 23);
        mtxtdatain.setBounds(102, 15, 82, 23);
        mtxtdataout.setBounds(102, 40, 82, 23);
        //mtxtdatain.setText("2000-01-01");
        mtxtdataout.setText("2000-01-01");
        mtxtvalcalc.setBounds(102, 65, 82, 23);
        mtxtvalfinala.setBounds(102, 90, 82, 23);
        mcomcota.setBounds(102, 115, 75, 23);
        mbtnvalcalc.setBounds(20, 160, 80, 23);
        mbtncalcfinal.setBounds(115, 160, 80, 23);
        mbtnvalcalc.setText("ValCalc");
        mbtncalcfinal.setText("CalcFin");

        //populare combobox
        // mcomcota.removeAllItems(); int x = comcota.getItemCount(); for(int k=0; k<x; k++) mcomcota.addItem(comcota.getItemAt(k));

        //
        stxtnume.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //1 = click st, 3 = click dr, ...
                if (e.getButton() == 3) {
                    temp = stxtnume;
                    pop.show(stxtnume, e.getX(), e.getY());
                }
            }
        });

        stxtvalcalc.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //1 = click st, 3 = click dr, ...
                if (e.getButton() == 3) {
                    temp = stxtvalcalc;
                    pop.show(stxtvalcalc, e.getX(), e.getY());
                }
            }
        });

        stxtcota.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //1 = click st, 3 = click dr, ...
                if (e.getButton() == 3) {
                    temp = stxtcota;
                    pop.show(stxtcota, e.getX(), e.getY());
                }
            }
        });

        stxtsumai.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //1 = click st, 3 = click dr, ...
                if (e.getButton() == 3) {
                    temp = stxtsumai;
                    pop.show(stxtsumai, e.getX(), e.getY());
                }
            }
        });

        stxtdatain.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //1 = click st, 3 = click dr, ...
                if (e.getButton() == 3) {
                    temp = stxtdatain;
                    pop.show(stxtdatain, e.getX(), e.getY());
                }
            }
        });

        stxtdataout.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //1 = click st, 3 = click dr, ...
                if (e.getButton() == 3) {
                    temp = stxtdataout;
                    pop.show(stxtdataout, e.getX(), e.getY());
                }
            }
        });

        //
        t = new Tools(fr); //aici se face conexiunea la bd

        //Adaug din meniu persoana;PERSOANA

        btnclose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frm.setVisible(false);
            }
        });


        btnres.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (frm.getTitle() == "Adaugare persoane") {
                    txtnume.setText("");
                    txtprenume.setText("");
                    txtcnp.setText("");
                } else if (frm.getTitle() == "Adaugare debite") {
                    txtdatain.setText("");
                    txtdataout.setText("");
                    txtsuma.setText("");
                    txtvalcalc.setText("");
                    lblzile.setText(String.format("<html><font color='red'>zile: __</font></html>"));
                } else if (frm.getTitle() == "Adaugare cote") {
                    txtcota.setText("");
                } else if (frm.getTitle() == "Adaugare plati") {

                    String s = cal.getDate().toString();

                    String[] v = s.split(" ");
                    String[] luni = "Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec".split(" ");
                    String[] lunile = "01 02 03 04 05 06 07 08 09 10 11 12".split(" ");

                    int j = 0;
                    for (int i = 0; i < 12; i++) {
                        if (v[1].equals(luni[i])) {
                            j = i;
                            break;
                        }
                    } //identificam indexul lunii curente

                    txtdataplata.setText(v[5] + "-" + lunile[j] + "-" + v[2]);
                    txtsumaachitata.setText("");
                }
            }
        });


        btnsaven.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int x = 0;

                if (frm.getTitle() == "Adaugare persoane") {
                    try {
                        if (t.ValidareAdaugarePersoane(txtcnp.getText()) == false) {
                            JOptionPane.showMessageDialog(new JFrame(), "CNP-ul '" + txtcnp.getText() + "' deja exista in baza !");
                            txtcnp.requestFocus();
                            return;
                        }
                    } catch (SQLException e1) {
                    }

                    t.InsertP(txtcnp.getText(), txtnume.getText(), txtprenume.getText());
                    x = t.CountPersoane();
                    //lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", x, x));
                    currentrow = x;
                    Tools.m("Adaugarea s-a facut cu succes");
                }

                else if (frm.getTitle() == "Adaugare debite") {
                    btnvalcalc.doClick();
                    if (Tools.ValidareAdaugareDebite(txtdatain.getText(), txtdataout.getText(), txtsuma.getText()) == false) {
                        JOptionPane.showMessageDialog(new JFrame(), "Introdu datele corecte! (yyyy-MM-dd)");
                        return;
                    }

                    String sdataout = txtdataout.getText();
                    if (sdataout.isEmpty()) sdataout = "2500-01-01";
                    String ssuma = txtsuma.getText();
                    if (ssuma.isEmpty()) ssuma = "0";

                    t.InsertD(txtdatain.getText(), sdataout, ssuma, txtvalcalc.getText(), compersoana.getSelectedIndex() + 1,
                            comcota.getSelectedIndex() + 1);

                    x = t.CountDebite();

                    currentrow = x;

                    try {
                        Date dIn = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdatain.getText());
                        Date dOut = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdataout.getText());

                        Long r = dOut.getTime() - dIn.getTime();
                        if (r <= 0) {
                            JOptionPane.showMessageDialog(new JFrame(), "data iesire <= data intrare! (yyyy-MM-dd)");
                            return;
                        }
                        //r = milisecunde

								/*
								 *   1 zi = 24 h; 1 h = 60 min = 3600 sec = 3600000 milisec
								 */

                        r = r / (24 * 60 * 60 * 1000); //zile

                        lblzile.setText(String.format("<html><font color='red'>zile: %s</font></html>", r));

                    } catch (Exception ex) {
                    }


                    Tools.m("Adaugarea s-a facut cu succes");
                }
			      /* adaugare Cota cu Set*/
                else if (frm.getTitle() == "Adaugare cote") {
                    Cota c = new Cota(t, currentrow);
                    c.Set(txtcota.getText());
                    //t.InsertC(txtcota.getText());
                    x = t.CountCote();
                    lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", x, Integer.toString(x)));
                    currentrow = x;
                    Tools.m("Adaugarea s-a facut cu succes");
                }

                else if (frm.getTitle() == "Adaugare plati")
                {
                    //validare
                    if (sold.getText().equals("-") || sold.getText().equals("0.0")) {
                        if (txtsumaachitata.getText().isEmpty() || (Float.parseFloat(txtsumaachitata.getText()) > Float.parseFloat(comdebite.getSelectedObjects()[0].toString()))) {
                            Tools.m("Introdu suma corecta!");
                            txtsumaachitata.requestFocus();
                            return;
                        }
                    } else {
                        if (txtsumaachitata.getText().isEmpty() || (Float.parseFloat(txtsumaachitata.getText()) > Float.parseFloat(sold.getText())) || (Float.parseFloat(txtsumaachitata.getText()) > Float.parseFloat(comdebite.getSelectedObjects()[0].toString()))) {
                            Tools.m("Introdu suma corecta!");
                            txtsumaachitata.requestFocus();
                            return;
                        }
                    }

                    //ID, IdDebite, IdPers, data_platii, suma_achitata         comdebite.getSelectedIndex() + 1
                    int idpers = compersoana.getSelectedIndex() + 1;
                    int y = t.CountDebitePers(idpers);
                    if(y ==0) return;
                    int iddebit = t.GetIdDebit(idpers, ((ComboItem)comdebite.getSelectedItem()).id);
                    //System.out.println(idpers + "-" + iddebit);
                    t.InsertPlata(iddebit, idpers, txtdataplata.getText(), txtsumaachitata.getText());

                    //try { Thread.sleep(1000);	} catch (InterruptedException e1) {	} //1 sec
                    x = t.CountPlati();
                    // lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", x, Integer.toString(x)));
                    currentrow = x;
                    //refresh comdebite
                    //x = comdebite.getSelectedIndex();
                    comdebite.removeAllItems();
                    //int id = compersoana.getSelectedIndex() + 1;// este indexul personei curente(alese)


                   // System.out.println(x + "-" + y);
                    String[] debite = t.GetDebitePers(idpers);

                    for (int j = 0; j < y; j++)
                    comdebite.addItem( new ComboItem(Integer.parseInt(debite[j].split(Pattern.quote("|"))[0]), debite[j].split(Pattern.quote("|"))[1]) );

                    comdebite.setSelectedIndex(iddebit-1);

                    Tools.m("Adaugarea s-a facut cu succes");
                }


                lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", x, x));

                //refresh
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }
        });


        btnsaver.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (currentrow < 1) return; //cazul 0/0

                // aici era validarea comuna

                if (frm.getTitle() == "Adaugare persoane") {
                    try {
                        if (t.ValidareAdaugarePersoane(txtcnp.getText()) == false) {
                            if (JOptionPane.showConfirmDialog(new JFrame(), "CNP-ul '" + txtcnp.getText() + "' deja exista in baza ! Continuati ?", "Atentie", JOptionPane.YES_NO_OPTION) != 0) {
                                txtcnp.requestFocus();
                                return;
                            }
                        }
                    } catch (SQLException e1) {
                    }

                    if (t.UpdateP(currentrow, txtcnp.getText(), txtnume.getText(), txtprenume.getText()))
                        Tools.m("Modificarea s-a facut cu succes");
                    else Tools.m("Eroare la modificare");
                }


                else if (frm.getTitle() == "Adaugare debite") {

                    //validare DEBITE
                    //if(txtsumaachitata.getText().isEmpty() || (Float.parseFloat(txtsumaachitata.getText()) > Float.parseFloat(comdebite.getSelectedObjects()[0].toString())))
                    //{ Tools.m("Introdu suma corecta!"); txtsumaachitata.requestFocus(); return; }

                    if (txtsuma.getText().isEmpty() || (Float.parseFloat(txtsuma.getText()) > Float.parseFloat(txtvalcalc.getText()))) {
                        Tools.m("Introdu suma corecta!");
                        txtsumaachitata.requestFocus();
                        return;
                    }


                    if (Tools.ValidareAdaugareDebite(txtdatain.getText(), txtdataout.getText(), txtsuma.getText()) == false) {
                        JOptionPane.showMessageDialog(new JFrame(), "Introdu datele corecte! (yyyy-MM-dd)");
                        return;
                    }
                    //
                    // Tools.m(txtvalcalc.getText());
                    if (t.UpdateD(currentrow, txtdatain.getText(), txtdataout.getText(), txtsuma.getText(), txtvalcalc.getText(),
                            compersoana.getSelectedIndex() + 1, comcota.getSelectedIndex() + 1))
                        Tools.m("Modificarea s-a facut cu succes");
                    else Tools.m("Eroare la modificare");
                } else if (frm.getTitle() == "Adaugare cote") {
                    if (t.UpdateP(currentrow, txtnume.getText(), txtprenume.getText(), txtcnp.getText()))
                        Tools.m("Modificarea s-a facut cu succes");
                    else Tools.m("Eroare la modificare");
                }

					 /*
					 else if(frm.getTitle() == "Adaugare plati")
				      {
						  int iddebit = t.GetIdDebit(compersoana.getSelectedIndex()+1, comdebite.getSelectedIndex()+1);
				    	  if(t.UpdatePlata( iddebit, txtdataplata.getText(), txtsumaachitata.getText()))
				    	  {
				    		//refresh comdebite
					    	  String olddeb = comdebite.getSelectedItem().toString(); //Tools.m("1");
					    	  comdebite.removeItemAt(comdebite.getSelectedIndex()); //Tools.m("2");
					    	  comdebite.addItem(Float.toString(Float.parseFloat(olddeb)-Float.parseFloat(txtsumaachitata.getText()))); //Tools.m("3");
					    	  comdebite.setSelectedIndex(comdebite.getItemCount()-1);//Tools.m("4");
					    	  Tools.m("Adaugarea s-a facut cu succes");
				    	  }
				    	  else Tools.m("Eroare la modificare");
				      }
					 */

                try {
                    Date dIn = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdatain.getText());
                    Date dOut = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdataout.getText());

                    Long r = dOut.getTime() - dIn.getTime();
                    if (r <= 0) {
                        JOptionPane.showMessageDialog(new JFrame(), "data iesire <= data intrare! (yyyy-MM-dd)");
                        return;
                    }
                    //r = milisecunde

								/*
								 *   1 zi = 24 h; 1 h = 60 min = 3600 sec = 3600000 milisec
								 */

                    r = r / (24 * 60 * 60 * 1000); //zile

                    lblzile.setText(String.format("<html><font color='red'>zile: %s</font></html>", r));

                } catch (Exception ex) {
                }

                //refresh tabel
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

        });


        btnnext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentrow < 1) return; //cazul 0/0

                if (frm.getTitle() == "Adaugare persoane") {
                    int x = t.CountPersoane();
                    if (x > currentrow) //daca nu suntem pe ultima linie in tabel
                    {
                        currentrow++;
                        lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(x)));
			    		  /*incarc urmatoare persoana*/
                        Persoana p = new Persoana(t, currentrow);// facem un Obiect Persoana caruia ii trimitem variabila t
                        p.Get();
                        txtcnp.setText(p.CNP);
                        txtnume.setText(p.Nume);
                        txtprenume.setText(p.Prenume);
                    } else Tools.m("Este ultima inregistrare");
                } else if (frm.getTitle() == "Adaugare debite") {
                    if (currentrow == 0) return; //cazul 0/0

                    int x = t.CountDebite();
                    if (x > currentrow) //daca nu suntem pe ultima linie in tabel
                    {
                        currentrow++;
                        lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(x)));
			    		  /*incarc urmatorul debit*/
                        Debit d = new Debit(t, currentrow);
                        d.Get();
                        txtdatain.setText(d.datain);
                        txtdataout.setText(d.dataout);
                        txtsuma.setText(d.suma);
                        txtvalcalc.setText(d.valoare);

                        compersoana.setSelectedItem(t.GetP()[Integer.parseInt(d.idpers) - 1]);
                        comcota.setSelectedItem(t.GetC()[Integer.parseInt(d.idcota) - 1]);
                    } else Tools.m("Este ultima inregistrare");

                    try {
                        Date dIn = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdatain.getText());
                        Date dOut = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdataout.getText());

                        Long r = dOut.getTime() - dIn.getTime();
                        if (r <= 0) {
                            JOptionPane.showMessageDialog(new JFrame(), "data iesire <= data intrare! (yyyy-MM-dd)");
                            return;
                        }
                        //r = milisecunde

								/*
								 *   1 zi = 24 h; 1 h = 60 min = 3600 sec = 3600000 milisec
								 */

                        r = r / (24 * 60 * 60 * 1000); //zile

                        lblzile.setText(String.format("<html><font color='red'>zile: %s</font></html>", r));

                    } catch (Exception ex) {
                    }

                } else if (frm.getTitle() == "Adaugare cote") {
                    int x = t.CountCote();
                    if (x > currentrow) //daca nu suntem pe ultima linie in tabel
                    {
                        currentrow++;
                        lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(x)));
			    		  /*incarc urmatoarea cota*/
                        Cota c = new Cota(t, currentrow);
                        c.Get();
                        txtcota.setText(c.valcota);
                    } else Tools.m("Este ultima inregistrare");
                } else if (frm.getTitle() == "Adaugare plati") {
                    int x = t.CountPlati();
                    if (x > currentrow) //daca nu suntem pe ultima linie in tabel
                    {
                        currentrow++;
                        lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(x)));
			    		  /*incarc urmatoarea plata*/
                        Plata p = new Plata(t, currentrow);
                        p.Get();
                        txtdataplata.setText(p.data_platii);
                        txtsumaachitata.setText(p.suma_achitata);
                        //selectare persoana a platii curente
                        if (t.CountPlati() > 0) compersoana.setSelectedIndex(t.GetPersPlata(currentrow) - 1);
                    } else Tools.m("Este ultima inregistrare");
                }
            }
        });


        btnback.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentrow == 0) return; //cazul 0/0

                if (frm.getTitle() == "Adaugare persoane") {
                    int x = t.CountPersoane();
                    if (currentrow > 1) //daca am cel putin 2 inregistrari
                    {
                        currentrow--;
                        lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(x)));
			    		  /*incarc urmatoare persoana*/
                        Persoana p = new Persoana(t, currentrow);// facem un Obiect Persoana caruia ii trimitem variabila t
                        p.Get();
                        txtcnp.setText(p.CNP);
                        txtnume.setText(p.Nume);
                        txtprenume.setText(p.Prenume);
                    } else Tools.m("Este prima inregistrare");
                } else if (frm.getTitle() == "Adaugare debite") {
                    int x = t.CountDebite();
                    if (currentrow > 1) //daca nu suntem pe ultima linie in tabel
                    {
                        currentrow--;
                        lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(x)));
			    		  /*incarc urmatorul debit*/
                        Debit d = new Debit(t, currentrow);
                        d.Get();
                        txtdatain.setText(d.datain);
                        txtdataout.setText(d.dataout);
                        txtsuma.setText(d.suma);
                        txtvalcalc.setText(d.valoare);
                        compersoana.setSelectedItem(t.GetP()[Integer.parseInt(d.idpers) - 1]);
                        comcota.setSelectedItem(t.GetC()[Integer.parseInt(d.idcota) - 1]);
                    } else Tools.m("Este prima inregistrare");

                    try {
                        Date dIn = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdatain.getText());
                        Date dOut = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdataout.getText());

                        Long r = dOut.getTime() - dIn.getTime();
                        if (r <= 0) {
                            JOptionPane.showMessageDialog(new JFrame(), "data iesire <= data intrare! (yyyy-MM-dd)");
                            return;
                        }
                        //r = milisecunde

								/*
								 *   1 zi = 24 h; 1 h = 60 min = 3600 sec = 3600000 milisec
								 */

                        r = r / (24 * 60 * 60 * 1000); //zile

                        lblzile.setText(String.format("<html><font color='red'>zile: %s</font></html>", r));

                    } catch (Exception ex) {
                    }

                } else if (frm.getTitle() == "Adaugare cote") {
                    int x = t.CountCote();
                    if (currentrow > 1) //daca nu suntem pe ultima linie in tabel
                    {
                        currentrow--;
                        lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(x)));
			    		  /*incarc urmatoare persoana*/
                        Cota c = new Cota(t, currentrow);
                        c.Get();
                        txtcota.setText(c.valcota);
                    } else Tools.m("Este prima inregistrare");
                } else if (frm.getTitle() == "Adaugare plati") {
                    int x = t.CountPlati();
                    if (currentrow > 1) //daca nu suntem pe ultima linie in tabel
                    {
                        currentrow--;
                        lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(x)));
			    		  /*incarc plata anterioara*/
                        Plata p = new Plata(t, currentrow);
                        p.Get();
                        txtdataplata.setText(p.data_platii);
                        txtsumaachitata.setText(p.suma_achitata);
                        //selectare persoana a platii curente
                        if (t.CountPlati() > 0) compersoana.setSelectedIndex(t.GetPersPlata(currentrow) - 1);
                    } else Tools.m("Este prima inregistrare");
                }
            }
        });


        btndel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //persoane
                if (frm.getTitle() == "Adaugare persoane") {

                    if (currentrow < 1) return; //cazul 0/0

                    if (t.DeleteP(currentrow))    //sterg din baza

                        try {
                            int x = t.CountPersoane();
                            if (currentrow > 1) currentrow--;

                            if (x > 0) //am sters din baza si au mai ramas persoane
                            {
                                lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", currentrow, x));
				        		  /*reincarc persoana 1*/
                                Persoana p = new Persoana(t, currentrow); // facem un Obiect Persoana caruia ii trimitem variabila t
                                p.Get();
                                txtcnp.setText(p.CNP);
                                txtnume.setText(p.Nume);
                                txtprenume.setText(p.Prenume);
                            } else //am sters din baza si nu a mai ramas nimic
                            {
                                currentrow = 0;
                                lblcr.setText("<html><font color='red'>0 / 0</font></html>");
                                btnres.doClick();
                                Tools.m("Nu mai sunt persoane in DB");
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
                        }

                }

                //debite
                else if (frm.getTitle() == "Adaugare debite") {

                    if (currentrow < 1) return; //cazul 0/0

                    if (t.DeleteD(currentrow))

                        try {
                            int x = t.CountDebite();
                            if (currentrow > 1) currentrow--;

                            if (x > 0) {
                                lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", currentrow, x));
			        		  /*reincarc debit 1*/
                                Debit d = new Debit(t, currentrow);// facem un Obiect Debit caruia ii trimitem variabila t
                                d.Get();
                                txtdatain.setText(d.datain);
                                txtdataout.setText(d.dataout);
                                txtsuma.setText(d.suma);
                                txtvalcalc.setText(d.valoare);
                                txtpersoana.setText(d.idpers);
                                txtcota.setText(d.idcota);

                                compersoana.setSelectedItem(t.GetP()[Integer.parseInt(d.idpers) - 1]);
                                comcota.setSelectedItem(t.GetC()[Integer.parseInt(d.idcota) - 1]);
                            } else {
                                currentrow = 0;
                                lblcr.setText("<html><font color='red'>0 / 0</font></html>");
                                btnres.doClick();
                                Tools.m("Nu mai sunt debite in DB");
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
                        }

                    try {
                        Date dIn = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdatain.getText());
                        Date dOut = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdataout.getText());

                        Long r = dOut.getTime() - dIn.getTime();
                        if (r <= 0) {
                            JOptionPane.showMessageDialog(new JFrame(), "data iesire <= data intrare! (yyyy-MM-dd)");
                            return;
                        }
                        //r = milisecunde

								/*
								 *   1 zi = 24 h; 1 h = 60 min = 3600 sec = 3600000 milisec
								 */

                        r = r / (24 * 60 * 60 * 1000); //zile

                        lblzile.setText(String.format("<html><font color='red'>zile: %s</font></html>", r));

                    } catch (Exception ex) {
                    }

                }

                //cote
                else if (frm.getTitle() == "Adaugare cote") {

                    if (currentrow < 1) return; //cazul 0/0

                    if (t.DeleteC(currentrow))

                        try {
                            int x = t.CountCote();
                            if (currentrow > 1) currentrow--;

                            if (x > 0) {
                                lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", currentrow, x));
				        		  /*reincarc cota 1*/
                                Cota c = new Cota(t, currentrow);// facem un Obiect Debit caruia ii trimitem variabila t
                                c.Get();
                                txtcota.setText(c.valcota);
                            } else {
                                currentrow = 0;
                                lblcr.setText("<html><font color='red'>0 / 0</font></html>");
                                btnres.doClick();
                                Tools.m("Nu mai sunt cote in DB");
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
                        }

                } else if (frm.getTitle() == "Adaugare plati") {

                    if (currentrow < 1) return; //cazul 0/0

                    if (t.DeletePl(currentrow))

                        try {
                            int x = t.CountPlati();
                            if (currentrow > 1) currentrow--;

                            if (x > 0) {
                                lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", currentrow, x));
				        		  /*reincarc plata 1*/
                                Plata p = new Plata(t, currentrow); // facem un Obiect Plata caruia ii trimitem variabila t
                                p.Get();
                                txtdataplata.setText(p.data_platii);
                                txtsumaachitata.setText(p.suma_achitata);
                                //selectare persoana a platii curente
                                compersoana.setSelectedIndex(t.GetPersPlata(currentrow) - 1);
                            } else {
                                currentrow = 0;
                                lblcr.setText("<html><font color='red'>0 / 0</font></html>");
                                btnres.doClick();
                                Tools.m("Nu mai sunt plati in DB");
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
                        }
                }

                //refresh tabel
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

        });


        btnvalcalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //if(txtdatain.getText() == "" || txtdataout.getText() == "" || txtsuma.getText() == "")
                if (Tools.ValidareAdaugareDebite(txtdatain.getText(), txtdataout.getText(), txtsuma.getText()) == false) {
                    JOptionPane.showMessageDialog(new JFrame(), "Introdu datele corecte! (yyyy-MM-dd)");
                    return;
                }

                try {
                    Date dIn = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdatain.getText());
                    Date dOut = null;
                    if (txtdataout.getText().isEmpty()) {
                        dOut = (Date) new SimpleDateFormat("yyyy-MM-dd").parse("2500-01-01");
                        txtvalcalc.setText("0");
                    } else {
                        dOut = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdataout.getText());

                        Long r = dOut.getTime() - dIn.getTime();
                        if (r <= 0) {
                            JOptionPane.showMessageDialog(new JFrame(), "data iesire <= data intrare! (yyyy-MM-dd)");
                            return;
                        }
                        //r = milisecunde

										/*
										 *   1 zi = 24 h; 1 h = 60 min = 3600 sec = 3600000 milisec
										 */

                        r = r / (24 * 60 * 60 * 1000); //zile


                        //=D6*0.06/100*F3
                        Locale.setDefault(Locale.US);
                        //DecimalFormat df = new DecimalFormat("#.##");
                        DecimalFormat df = new DecimalFormat(".00");
                        df.setRoundingMode(RoundingMode.UP);

                        Double p = Double.parseDouble(txtsuma.getText()) * Double.parseDouble(comcota.getSelectedItem().toString()) / 100 * (new Double(r));
                        Double f = Double.parseDouble(txtsuma.getText()) + p;
                        //Tools.m(p.toString());
                        txtvalcalc.setText(df.format(f));
                    }

                } //catch (ParseException e) { e.printStackTrace(); }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
                }


            }
        });


        btnmore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mtxtdatain.setText(txtdatain.getText());
                mtxtdataout.setText(txtdataout.getText());
                mlblvalfinala.setVisible(false);
                mtxtvalfinala.setVisible(false);
                mbtncalcfinal.setEnabled(false);
                more.setVisible(true);
            }
        });


        mbtnvalcalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                if (Tools.ValidareAdaugareDebite(mtxtdatain.getText(), mtxtdataout.getText(), mtxtvalcalc.getText()) == false) {
                    JOptionPane.showMessageDialog(new JFrame(), "Introdu datele corecte! (yyyy-MM-dd)");
                    return;
                }

                try {
                    Date dIn = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(mtxtdatain.getText());
                    Date dOut = null;
                    if (mtxtdataout.getText().isEmpty()) {
                        dOut = (Date) new SimpleDateFormat("yyyy-MM-dd").parse("2500-01-01");
                        mtxtvalcalc.setText("0"); //?
                    } else {
                        dOut = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(mtxtdataout.getText());

                        Long r = dOut.getTime() - dIn.getTime();
                        if (r <= 0) {
                            JOptionPane.showMessageDialog(new JFrame(), "data iesire <= data intrare! (yyyy-MM-dd)");
                            return;
                        }

                        r = r / (24 * 60 * 60 * 1000); //zile

                        Locale.setDefault(Locale.US);
                        DecimalFormat df = new DecimalFormat(".00");
                        df.setRoundingMode(RoundingMode.UP);

                        Double p = Double.parseDouble(txtsuma.getText()) * Double.parseDouble(mcomcota.getSelectedItem().toString()) / 100 * (new Double(r));
                        //Double f = Double.parseDouble(txtsuma.getText()) ;
                        //Tools.m(p.toString());
                        mtxtvalcalc.setText(df.format(p));
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
                }


            }
        });


        mbtncalcfinal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                switch (JOptionPane.showConfirmDialog(more, "Salvati noua valoare ?", "Confirmare", JOptionPane.YES_NO_CANCEL_OPTION)) {

                    case JOptionPane.YES_OPTION:

                        Locale.setDefault(Locale.US);
                        DecimalFormat df = new DecimalFormat(".00");
                        df.setRoundingMode(RoundingMode.UP);

                        Float v = Float.parseFloat(txtvalcalc.getText()) + Float.parseFloat(mtxtvalcalc.getText());

                        txtvalcalc.setText(df.format(v));
                        //more.setVisible(false);

                        break;

                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        more.setVisible(false);
                        break;
                }


            }
        });


        mtxtvalcalc.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                if (mtxtvalcalc.getText().isEmpty()) mbtncalcfinal.setEnabled(false);
                else mbtncalcfinal.setEnabled(true);
            }

            public void removeUpdate(DocumentEvent e) {
                if (mtxtvalcalc.getText().isEmpty()) mbtncalcfinal.setEnabled(false);
                else mbtncalcfinal.setEnabled(true);
            }

            public void insertUpdate(DocumentEvent e) {
                if (mtxtvalcalc.getText().isEmpty()) mbtncalcfinal.setEnabled(false);
                else mbtncalcfinal.setEnabled(true);
            }

        });


        compersoana.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                    //populare debite cu debitele primei persoane din compersoana
                    comdebite.removeAllItems();
                    int id = compersoana.getSelectedIndex() + 1;// este indexul personei curente(alese)

                    //System.out.println(id);

                    int y = t.CountDebitePers(id);//numar debitele corespunzatore pentru un IdPers ales(am IdPers cu index 1 cinci si cinci debite adica 5 IdDebite)
                    if (frm.getTitle() == "Adaugare plati") {
                        //Tools.m("a");
                        if (y == 0) {
                            btnsaven.setEnabled(false);
                            btnsaver.setEnabled(false);
                        }// nu am debit pt. persoana aleasa
                        else {
                            String[] debite = t.GetDebitePers(id);
                            //for (int j = 0; j < y; j++) comdebite.addItem(debite[j]);
                            for (int j = 0; j < y; j++)
                                comdebite.addItem(new ComboItem(Integer.parseInt(debite[j].split(Pattern.quote("|"))[0]), debite[j].split(Pattern.quote("|"))[1]));

                            comdebite.setSelectedIndex(0);
                            btnsaven.setEnabled(true);
                            btnsaver.setEnabled(true);
                        }

                    } else {
                        btnsaven.setEnabled(true);
                        btnsaver.setEnabled(true);
                    }
                }

        });


      /*  comdebite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Object elem = (Object) comdebite.getSelectedItem();
                Tools.m(Integer.toString(comdebite.getSelectedIndex()));
            }
        });
        */
        //actualizez sold din fr plati care vine din fr debite; sunnt in frm
        comdebite.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                //Tools.m(comdebite.getSelectedItem().toString());
                if (comdebite.getItemCount() == 0)
                {
                    sold.setText("-"); //NU AM DEBIT
                }
                //else sold.setText(t.GetSold(comdebite.getSelectedItem().toString(),0));
                else
                {
                    JComboBox comboBox = (JComboBox) e.getSource();//vad pe ce combo sunt
                    //System.out.println(comboBox.getName());
                    // Tools.m(comboBox.getName());
                    //comboBox.addItem(new Item(0, "111"));
                    ComboItem item = (ComboItem) comboBox.getSelectedItem();//il trimit in Tools sa-mi aduca selectia itemului(fost string) cu soldurile
                    //System.out.println(item.index + " : " + item.text);
                    sold.setText(t.GetSold(item.text, item.id));
                }
            }


        });


        //Meniu principal
        mb = new JMenuBar();
        fr.setJMenuBar(mb);
        m = new JMenu("Fisier");
        m.setMnemonic('f');
        mb.add(m);

        mi = new JMenuItem("Iesire");
        mi.setMnemonic('e');
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.this.dispose();
                System.exit(0);
            }
        });

        m.add(mi);

        m = new JMenu("Adaugare");
        m.setMnemonic('e');
        mb.add(m);

        mi = m.add(new JMenuItem("Persoane", 't'));
        // mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));

        lblcr.setBounds(350, 10, 100, 23);

        //Adaugare persoana
        mi.addActionListener(new ActionListener() //load
        {
            public void actionPerformed(ActionEvent e) {

                lblsold.setVisible(false);
                sold.setVisible(false);

                lbldataplata.setVisible(false);
                lblsumaachitata.setVisible(false);
                lbldebite.setVisible(false);
                comdebite.setVisible(false);
                lblnume.setVisible(true);
                lblprenume.setVisible(true);
                lblcnp.setVisible(true);
                lblcota.setVisible(false);
                txtnume.setVisible(true);
                txtprenume.setVisible(true);
                txtcnp.setVisible(true);
                txtcota.setVisible(false);
                txtdatain.setVisible(false);
                txtdataout.setVisible(false);
                txtsuma.setVisible(false);
                txtpersoana.setVisible(false);
                txtvalcalc.setVisible(false);
                compersoana.setVisible(false);
                comcota.setVisible(false);
                lbldatain.setVisible(false);
                lbldataout.setVisible(false);
                lblsuma.setVisible(false);
                lblpersoana.setVisible(false);
                lblvalcalc.setVisible(false);

                lbldataplata.setVisible(false);
                lblsumaachitata.setVisible(false);
                txtdataplata.setVisible(false);
                txtsumaachitata.setVisible(false);

                lblnume.setBounds(5, 10, 50, 23);
                lblprenume.setBounds(5, 35, 80, 23);
                lblcnp.setBounds(5, 60, 50, 23);

                txtnume.setBounds(70, 10, 260, 23);
                txtprenume.setBounds(70, 35, 260, 23);
                txtcnp.setBounds(70, 60, 260, 23);

                btnnext.setBounds(30, 100, 80, 23);
                btnback.setBounds(30, 135, 80, 23);
                btnsaven.setBounds(140, 100, 80, 23);
                btnsaver.setBounds(140, 135, 80, 23);
                btndel.setBounds(250, 100, 80, 23);
                btnres.setBounds(250, 135, 80, 23);
                btnclose.setBounds(140, 200, 80, 23);

                btnsaver.setVisible(true);
                btnmore.setVisible(false);

                frm.setTitle("Adaugare persoane");
                frm.setSize(450, 300);
                frm.setVisible(true);

                int x = t.CountPersoane();

                if (x > 0) {
                    currentrow = 1; //0 este cazul cind nu sunt pers in baza
                    lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(x)));
                    //apelez clasa persoana
                    Persoana p = new Persoana(t, currentrow);// facem un Obiect Persoana caruia ii trimitem variabila t
                    p.Get();
                    txtcnp.setText(p.CNP);
                    txtnume.setText(p.Nume);
                    txtprenume.setText(p.Prenume);
                } else {
                    currentrow = 0;// afisez 0/0
                    lblcr.setText(String.format("<html><font color='red'>0 / 0</font></html>"));
                }

            }
        });


        mi = m.add(new JMenuItem("Cote", 'c'));
        // mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                lblsold.setVisible(false);
                sold.setVisible(false);

                lbldataplata.setVisible(false);
                lblsumaachitata.setVisible(false);
                lbldebite.setVisible(false);
                comdebite.setVisible(false);

                lblnume.setVisible(false);
                lblprenume.setVisible(false);
                lblcnp.setVisible(false);
                lblcota.setVisible(true);
                lbldatain.setVisible(false);
                lbldataout.setVisible(false);
                lblsuma.setVisible(false);
                lblpersoana.setVisible(false);
                lblvalcalc.setVisible(false);
                lblcr.setBounds(350, 30, 100, 23);

                txtnume.setVisible(false);
                txtprenume.setVisible(false);
                txtcnp.setVisible(false);
                txtcota.setVisible(true);
                txtdatain.setVisible(false);
                txtdataout.setVisible(false);
                txtsuma.setVisible(false);
                txtpersoana.setVisible(false);
                txtvalcalc.setVisible(false);
                compersoana.setVisible(false);
                comcota.setVisible(false);

                lbldataplata.setVisible(false);
                lblsumaachitata.setVisible(false);
                txtdataplata.setVisible(false);
                txtsumaachitata.setVisible(false);

                lblcota.setBounds(20, 30, 50, 23);
                txtcota.setBounds(70, 30, 250, 23);

                btnnext.setBounds(30, 100, 80, 23);
                btnback.setBounds(30, 135, 80, 23);
                btnsaven.setBounds(140, 100, 80, 23);
                btnsaver.setBounds(140, 135, 80, 23);
                btndel.setBounds(250, 100, 80, 23);
                btnres.setBounds(250, 135, 80, 23);
                btnclose.setBounds(140, 200, 80, 23);

                btnsaver.setVisible(true);
                btnmore.setVisible(false);

                frm.setTitle("Adaugare cote");
                frm.setSize(450, 300);
                frm.setVisible(true);

                if (t.CountCote() > 0) {
                    currentrow = 1;
                    //lblCR.setText(String.format("<html>%s<font color='red'>%s</font></html>", lblCR.getText(), Integer.toString(currentrow)));
                    lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(t.CountCote())));
                    //apelez clasa persoana

                    Cota c = new Cota(t, currentrow);// facem un Obiect Persoana caruia ii trimitem variabila t
                    c.Get();
                    txtcota.setText(c.valcota);
                } else {
                    lblcr.setText(String.format("<html><font color='red'>0 / 0</font></html>"));
                    currentrow = 0;
                }
            }
        });


        mi = m.add(new JMenuItem("Debite", 'p'));
        //mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                lblsold.setVisible(false);
                sold.setVisible(false);

                lbldebite.setVisible(false);
                comdebite.setVisible(false);
                lbldataplata.setVisible(false);
                lblsumaachitata.setVisible(false);
                txtdataplata.setVisible(false);
                txtsumaachitata.setVisible(false);
                lblnume.setVisible(false);
                lblprenume.setVisible(false);
                lblcnp.setVisible(false);
                lblcota.setVisible(false);
                lbldatain.setBounds(20, 5, 80, 23);
                lbldataout.setBounds(20, 30, 80, 23);
                lblsuma.setBounds(20, 55, 80, 23);
                lblpersoana.setBounds(20, 80, 80, 23);
                lblcota.setBounds(20, 105, 80, 23);
                lblvalcalc.setBounds(20, 130, 80, 23);
                lbldatain.setVisible(true);
                lbldataout.setVisible(true);
                lblsuma.setVisible(true);
                lblpersoana.setVisible(true);
                lblvalcalc.setVisible(true);
                lblcr.setBounds(375, 10, 100, 23);
                lblzile.setBounds(375, 30, 100, 23); //lblzile.setText("ok");
                txtnume.setVisible(false);
                txtprenume.setVisible(false);
                txtcnp.setVisible(false);
                txtcota.setVisible(false);
                txtdatain.setVisible(true);
                txtdataout.setVisible(true);
                txtsuma.setVisible(true);
                txtpersoana.setVisible(false);
                txtvalcalc.setVisible(true);

                txtdatain.setBounds(100, 5, 250, 23);
                txtdataout.setBounds(100, 30, 250, 23);
                txtsuma.setBounds(100, 55, 250, 23);
                txtpersoana.setBounds(100, 80, 250, 23);
                txtcota.setBounds(100, 105, 250, 23);
                txtvalcalc.setBounds(100, 130, 250, 23);
                btnvalcalc.setBounds(140, 160, 80, 23);

                compersoana.setBounds(100, 80, 250, 23);
                comcota.setBounds(100, 105, 250, 23);
                //compersoana.updateUI(); comcota.updateUI();
                compersoana.setVisible(true);
                comcota.setVisible(true);

                lblcota.setVisible(true);
                btnvalcalc.setVisible(true);

                btnnext.setBounds(30, 200, 80, 23);
                btnback.setBounds(30, 235, 80, 23);
                btnsaven.setBounds(140, 200, 80, 23);
                btnsaver.setBounds(140, 235, 80, 23);
                btndel.setBounds(250, 200, 80, 23);
                btnres.setBounds(250, 235, 80, 23);
                btnclose.setBounds(140, 280, 80, 23);
                btnmore.setBounds(225, 160, 80, 23);

                //btnsaver.setVisible(false); btnmore.setVisible(true);
                btnsaver.setVisible(true);
                btnmore.setVisible(true);
                frm.setTitle("Adaugare debite");
                frm.setSize(450, 370);
                frm.setVisible(true);

                if (t.CountDebite() > 0) {

                    currentrow = 1;
                    lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(t.CountDebite())));
                    //apelez clasa Debit
                    Debit d = new Debit(t, currentrow); // facem un Obiect Debit caruia ii trimitem variabila t
                    d.Get();
                    txtdatain.setText(d.datain);
                    txtdataout.setText(d.dataout);
                    txtsuma.setText(d.suma);
                    txtvalcalc.setText(d.valoare);

                } else {
                    //System.out.println("ok");
                    lblcr.setText(String.format("<html><font color='red'>0 / 0</font></html>"));
                    currentrow = 0;
                }

                //populare combo
                Refresh();

                try {
                    Date dIn = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdatain.getText());
                    Date dOut = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(txtdataout.getText());

                    Long r = dOut.getTime() - dIn.getTime();
                    if (r <= 0) {
                        JOptionPane.showMessageDialog(new JFrame(), "data iesire <= data intrare! (yyyy-MM-dd)");
                        return;
                    }
                    //r = milisecunde

							/*
							 *   1 zi = 24 h; 1 h = 60 min = 3600 sec = 3600000 milisec
							 */

                    r = r / (24 * 60 * 60 * 1000); //zile

                    lblzile.setText(String.format("<html><font color='red'>zile: %s</font></html>", r));

                } catch (Exception ex) {
                }

                //Object o = new Object();
                //Object o = null;
                // o = new Object();
                // o = new Object();
                // o = new Object();

            }
        });

	   /*Aici adaug submeniul Plati la meniul Adaugare*/

        mi = m.add(new JMenuItem("Plati", 'p'));

        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                lblsold.setVisible(true);
                sold.setVisible(true);
                lblcnp.setVisible(false);
                lblcota.setVisible(false);
                lblnume.setVisible(false);
                lblprenume.setVisible(false);
                lbldatain.setVisible(false);
                lbldataout.setVisible(false);
                lblvalcalc.setVisible(false);

                lbldataplata.setBounds(20, 5, 80, 23);
                lblsumaachitata.setBounds(20, 30, 80, 23);
                //lblsuma.setBounds(20, 55, 80, 23);
                lblpersoana.setBounds(20, 55, 80, 23);
                lbldebite.setBounds(20, 80, 80, 23);
                lblsold.setBounds(20, 105, 80, 23);
                sold.setBounds(100, 105, 250, 23);
                //lblcota.setBounds(20, 105, 80, 23);
                lblvalcalc.setBounds(20, 130, 80, 23);

                lbldataplata.setVisible(true);
                lblsumaachitata.setVisible(true);
                lblsuma.setVisible(false);
                lblpersoana.setVisible(true);
                lbldebite.setVisible(true);
                lblcr.setBounds(375, 10, 100, 23);

                txtnume.setVisible(false);
                txtprenume.setVisible(false);
                txtcnp.setVisible(false);
                txtcota.setVisible(false);
                txtsuma.setVisible(false);
                txtpersoana.setVisible(false);
                txtvalcalc.setVisible(false);
                txtdatain.setVisible(false);
                txtdataout.setVisible(false);
                txtdataplata.setVisible(true);
                txtsumaachitata.setVisible(true);


			/*String s = cal.getDate().toString();

			String[] v = s.split(" ");
			String[] luni = "Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec".split(" ");
			String[] lunile = "01 02 03 04 05 06 07 08 09 10 11 12".split(" ");

			int j = 0; for(int i = 0; i < 12; i++) { if(v[1].equals(luni[i])) { j = i; break; } } //identificam indexul lunii curente

			 txtdataplata.setText(v[5] + "-" + lunile[j] + "-" + v[2]);*/

                txtsumaachitata.setText("");

                txtdataplata.setBounds(100, 5, 250, 23);
                txtsumaachitata.setBounds(100, 30, 250, 23); //txtsuma.setBounds(100, 55, 250, 23);
                txtpersoana.setBounds(100, 80, 250, 23);
                txtcota.setBounds(100, 105, 250, 23);
                txtvalcalc.setBounds(100, 130, 250, 23);
                btnvalcalc.setVisible(false); //btnvalcalc.setBounds(140, 160, 80, 23);

                compersoana.setBounds(100, 55, 250, 23);
                comdebite.setBounds(100, 80, 250, 23);
                sold.setBounds(100, 105, 250, 23); //lblsold.setEditable(false);
                //compersoana.updateUI(); comcota.updateUI();
                compersoana.setVisible(true);
                comcota.setVisible(false);
                comdebite.setVisible(true);


                Refresh();

                int id = compersoana.getSelectedIndex() + 1;// este indexul personei curente(alese)
                int y = t.CountDebitePers(id);//numar debitele corespunzatore pentru un IdPers ales(am IdPers cu index 1 cinci si cinci debite adica 5 IdDebite)

                if (frm.getTitle() == "Adaugare plati") {
                    //Tools.m("a");
                    if (y == 0) {
                        btnsaven.setEnabled(false);
                    }//btnsaver.setEnabled(false); nu am debit pt. persoana aleasa
                    else {
                        String[] debite = t.GetDebitePers(id);
                        for (int j = 0; j < y; j++)
                            comdebite.addItem(new ComboItem(Integer.parseInt(debite[j].split(Pattern.quote("|"))[0]), debite[j].split(Pattern.quote("|"))[1]));
                        btnsaven.setEnabled(true); //btnsaver.setEnabled(true);
                    }
                } else {
                    btnsaven.setEnabled(true);
                } //btnsaver.setEnabled(true);


                btnnext.setBounds(30, 200, 80, 23);
                btnback.setBounds(30, 235, 80, 23);
                btnsaven.setBounds(140, 200, 80, 23);
                btnsaver.setBounds(140, 235, 80, 23);
                btndel.setBounds(250, 200, 80, 23);
                btnres.setBounds(250, 235, 80, 23);
                btnclose.setBounds(140, 280, 80, 23);

                //btnsaven.setEnabled(true);
                btnsaver.setVisible(false);
                btnmore.setVisible(false);


                frm.setTitle("Adaugare plati");
                frm.setSize(450, 370);
                frm.setVisible(true);


                if (t.CountPlati() > 0) {
                    currentrow = 1;
                    lblcr.setText(String.format("<html><font color='red'>%s / %s</font></html>", Integer.toString(currentrow), Integer.toString(t.CountPlati())));
                    //apelez clasa Plata
                    Plata p = new Plata(t, currentrow); // facem un Obiect Plata caruia ii trimitem variabila t
                    p.Get();
                    txtdataplata.setText(p.data_platii);
                    txtsumaachitata.setText(p.suma_achitata);

                } else {
                    lblcr.setText(String.format("<html><font color='red'>0 / 0</font></html>"));
                    currentrow = 0;
                }

                Refresh();

            }
        });




	  /*Adaug meniu Vizualizare */

        m = new JMenu("Vizualizare");
        m.setMnemonic('e');
        mb.add(m);

        mi = m.add(new JMenuItem("Persoane", 'p'));
        // mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(new JFrame(), "VP");

                //tabel
                tab.setColumnCount(0); //sterg coloanele
                tab.addColumn("ID");
                tab.addColumn("CN‎P");
                tab.addColumn("Nume");
                tab.addColumn("Prenume");// fac header tabel

                tab.setNumRows(0); //sterg tot din tabel

                for (int i = 0; i < t.nr; i++) tab.addRow(t.GetPersoane()[i]);// populez tabelul

                jtab = new JTable(tab);
                //jtab.setEnabled(false);
                sp = new JScrollPane(jtab);  //construim sp-ul pe baza lui jtab
                viz.addd(sp);

                viz.settitle("Vizualizare Persoane");
                viz.setsize(500, 300);
                viz.setvisible(true);

            }
        });

        mi = m.add(new JMenuItem("Cote", 'c'));
        // mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tabel
                tab.setColumnCount(0); //sterg coloanele
                tab.addColumn("ID Cote");
                tab.addColumn("Valori Cote"); // fac header tabel

                tab.setNumRows(0); //sterg tot din tabel

                for (int i = 0; i < t.nr; i++) tab.addRow(t.GetCote()[i]);// populez tabelul

                jtab = new JTable(tab);
                //jtab.setEnabled(false);
                sp = new JScrollPane(jtab);  //construim sp-ul pe baza lui jtab

                viz.addd(sp);

                viz.settitle("Vizualizare Cote");
                viz.setSize(370, 300);
                viz.setvisible(true);
            }
        });

        mi = m.add(new JMenuItem("Debite", 'd'));
        //mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
        //mi2.addActionListener(l);
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                tab.setColumnCount(0); //sterg coloanele
                tab.addColumn("ID Debite");
                tab.addColumn("Data In");
                tab.addColumn("Data Out"); // fac header tabel
                tab.addColumn("Suma");
                tab.addColumn("Valoare");
                tab.addColumn("Id Pers");

                tab.setNumRows(0); //sterg tot din tabel

                for (int i = 0; i < t.nr; i++) tab.addRow(t.GetDebite()[i]);// populez tabelul

                jtab = new JTable(tab);

                //jtab.setEnabled(false);

                sp = new JScrollPane(jtab);

                viz.addd(sp);

                viz.settitle("Vizualizare Debite");
                viz.setSize(370, 300);
                viz.setvisible(true);
            }
        });

	    /*Vizualizare plati*/
        mi = m.add(new JMenuItem("Plati", 'p'));
        //mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
        //mi2.addActionListener(l);
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                tab.setColumnCount(0); //sterg coloanele
                tab.addColumn("ID Debite");
                tab.addColumn("ID Pers");
                tab.addColumn("Data platii"); // fac header tabel
                tab.addColumn("Suma achitata");

                tab.setNumRows(0); //sterg tot din tabel

                for (int i = 0; i < t.nr; i++) tab.addRow(t.GetPlati()[i]);// populez tabelul

                jtab = new JTable(tab);

                //jtab.setEnabled(false);

                sp = new JScrollPane(jtab);

                viz.addd(sp);

                viz.settitle("Vizualizare Plati");
                viz.setSize(370, 300);
                viz.setvisible(true);
            }
        });


        //Meniu Sold; submeniu Vezi Sold
        m = new JMenu("Sold");
        m.setMnemonic('l');// mb.add(Box.createHorizontalGlue());
        mb.add(m);
        mi = m.add(new JMenuItem("Vezi Sold", 't')); //m.setAlignmentX(RIGHT_ALIGNMENT);

        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //tabel sold ----

                tab.setColumnCount(0); //sterg coloanele
                tab.addColumn("Nume"); // fac header tabel
                tab.addColumn("Data in");
                tab.addColumn("Data out");
                tab.addColumn("Data platii");
                tab.addColumn("Debit");
                tab.addColumn("Accesorii");
                tab.addColumn("Debit_acces");
                tab.addColumn("Suma achit.");
                tab.addColumn("Sold");
                tab.setNumRows(0); //sterg tot din tabel

                if (!stxtnume.getText().isEmpty() || !stxtdatain.getText().isEmpty() || !stxtdataout.getText().isEmpty()) {

                    t.sold(stxtnume.getText(), stxtdatain.getText(), stxtdataout.getText()); //dupa astea 3 se face selectia

                    for (int i = 0; i <= t.nr; i++) tab.addRow(t.ob[i]); //populez tabelul

                    for (int i = 0; i < 9; i++) {
                        tab.setValueAt("<html><b><font color='red'>" + tab.getValueAt(t.nr, i) + "</font></b></html>", t.nr, i);
                        //jtab.getTableCellRendererComponent(jtab, null, false, false, t.nr, i);
                    }

                }

                jtab = new JTable(tab);
                //jtab.getTableHeader().setForeground(Color.CYAN); jtab.getTableHeader().setBackground(Color.YELLOW);
                //  jtab.getCellRenderer(t.nr, 7).getTableCellRendererComponent(jtab, jtab.getValueAt(t.nr, 7), false, false, t.nr, 7).setBackground(Color.RED);

                sp = new JScrollPane(jtab);

                //sp.getViewport().setBackground(Color.BLUE);

                viz.addd(sp);

                viz.settitle("Vizualizare Sold");
                viz.setSize(700, 400);
                viz.setvisible(true);

            }
        });


        //Meniu Export
        m = new JMenu("Export");
        m.setMnemonic('x');
        mb.add(Box.createHorizontalGlue());
        mb.add(m);
        mi = m.add(new JMenuItem("To CSV", 't')); //m.setAlignmentX(RIGHT_ALIGNMENT);

        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Tools.ExportCsv(t.ob, t.dela, t.panala);
                System.gc(); //optimizare memorie RAM
            }
        });

        //1
        stxtnume.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void removeUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void insertUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            //

        });


        //2
        stxtvalcalc.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void removeUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void insertUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

        });


        //3
        stxtcota.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void removeUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void insertUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

        });


        //4
        stxtsumai.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void removeUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void insertUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

        });


        //5
        stxtdatain.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void removeUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void insertUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

        });


        stxtdatain.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                stxtdatain.setBackground(new Color(206, 239, 255));
                fr.index = 10;
            }

            public void focusLost(FocusEvent e) {
                stxtdatain.setBackground(Color.WHITE);
            }

        });


        //6
        stxtdataout.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void removeUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

            public void insertUpdate(DocumentEvent e) {
                t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
            }

        });


        //txtdataplata
      /* txtdataplata.getDocument().addDocumentListener(new DocumentListener()
       {

        public void changedUpdate(DocumentEvent e)
        { btnsaven.setEnabled(!txtdataplata.getText().isEmpty()); btnsaver.setEnabled(!txtdataplata.getText().isEmpty()); }
        public void removeUpdate(DocumentEvent e)
        { btnsaven.setEnabled(!txtdataplata.getText().isEmpty()); btnsaver.setEnabled(!txtdataplata.getText().isEmpty()); }
        public void insertUpdate(DocumentEvent e)
        { btnsaven.setEnabled(!txtdataplata.getText().isEmpty()); btnsaver.setEnabled(!txtdataplata.getText().isEmpty()); }

       });
       */

        stxtdataout.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                stxtdataout.setBackground(new Color(206, 239, 255));
                fr.index = 12;
            }

            public void focusLost(FocusEvent e) {
                stxtdataout.setBackground(Color.WHITE);
            }
        });


        //final
        fr.setvisible(true);
        t.tabel(jtab, sp, stxtnume.getText(), stxtvalcalc.getText(), stxtcota.getText(), stxtsumai.getText(), stxtdatain.getText(), stxtdataout.getText(), fr);
        listener = new MyDateListener(fr);
        cal.addDateListener(listener);

    }//end constructor JMain

    //start aplicatie
    public static void main(String arg[]) {
        new Main();
    }

}
