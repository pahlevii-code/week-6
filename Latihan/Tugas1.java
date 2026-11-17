import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tugas1 extends JFrame {
    // Komponen 1: Form Registrasi
    private JTextField txtNama, txtEmail;
    private JPasswordField txtSandi;
    private JRadioButton rbLaki, rbPerempuan;
    private JComboBox<String> cbProdi;
    private JCheckBox chkCoding, chkDesain, chkJaringan, chkAI, chkFilm, chkMembaca;
    private JLabel lblJumlahHobi;

    // Komponen 2: Editor Teks
    private JTextArea area;
    private JFileChooser fc;
    private JSlider slFont;

    // Komponen 3: Data Mahasiswa
    private JTextField txtNim, txtMhsNama, txtIpk;
    private JComboBox<String> cbMhsProdi;
    private JSpinner spAngkatan;
    private DefaultTableModel model;
    private JTable tabel;
    private JProgressBar barIpk;

    public Tugas1() {
        super("Tugas 1 - Java Swing (UISI Informatika)");
        setSize(760, 560);
        setLocationRelativeTo(null);

        // Nilai + PPT Hal. 6, 13, 25: DO_NOTHING_ON_CLOSE & WindowAdapter
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int j = JOptionPane.showConfirmDialog(
                        Tugas1.this,
                        "Tutup aplikasi Tugas 1?",
                        "Konfirmasi Keluar",
                        JOptionPane.YES_NO_OPTION
                );
                if (j == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        setupMenuBar();

        JTabbedPane tab = new JTabbedPane();
        tab.addTab("Form Registrasi", buatTabRegistrasi());
        tab.addTab("Editor Teks", buatTabEditor());
        tab.addTab("Data Mahasiswa", buatTabMahasiswa());

        add(tab);
    }

    // ==================== MENU BAR (PPT Hal. 15) ====================
    private void setupMenuBar() {
        JMenuBar bar = new JMenuBar();

        // Menu File + Accelerator (Ctrl+N, Ctrl+O, Ctrl+S)
        JMenu file = new JMenu("File");
        file.setMnemonic('F');

        JMenuItem baru = new JMenuItem("Baru");
        baru.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        baru.addActionListener(e -> aksiBaru());

        JMenuItem buka = new JMenuItem("Buka");
        buka.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        buka.addActionListener(e -> aksiBuka());

        JMenuItem simpan = new JMenuItem("Simpan");
        simpan.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        simpan.addActionListener(e -> aksiSimpan());

        JMenuItem keluar = new JMenuItem("Keluar");
        keluar.addActionListener(e -> {
            int j = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (j == JOptionPane.YES_OPTION) System.exit(0);
        });

        file.add(baru);
        file.add(buka);
        file.add(simpan);
        file.addSeparator();
        file.add(keluar);

        // Menu Edit + JColorChooser
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic('E');

        JMenuItem potong = new JMenuItem("Potong");
        potong.addActionListener(e -> area.cut());

        JMenuItem salin = new JMenuItem("Salin");
        salin.addActionListener(e -> area.copy());

        JMenuItem tempel = new JMenuItem("Tempel");
        tempel.addActionListener(e -> area.paste());

        JMenuItem warna = new JMenuItem("Warna Latar Editor...");
        warna.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Pilih Warna Latar", area.getBackground());
            if (c != null) area.setBackground(c);
        });

        edit.add(potong);
        edit.add(salin);
        edit.add(tempel);
        edit.addSeparator();
        edit.add(warna);

        bar.add(file);
        bar.add(edit);
        setJMenuBar(bar);
    }

    // ==================== 1. FORM REGISTRASI (PPT Hal. 9-13) ====================
    private JPanel buatTabRegistrasi() {
        JPanel f = new JPanel(new GridBagLayout());
        f.setBorder(BorderFactory.createTitledBorder("Registrasi Akun Mahasiswa"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.anchor = GridBagConstraints.WEST;

        // Nama
        c.gridx = 0; c.gridy = 0;
        f.add(new JLabel("Nama:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        txtNama = new JTextField(18);
        txtNama.setToolTipText("Masukkan nama lengkap");
        f.add(txtNama, c);

        // Email
        c.gridx = 0; c.gridy = 1; c.fill = GridBagConstraints.NONE; c.weightx = 0;
        f.add(new JLabel("Email:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        txtEmail = new JTextField(18);
        txtEmail.setToolTipText("Masukkan alamat email aktif");
        f.add(txtEmail, c);

        // Sandi
        c.gridx = 0; c.gridy = 2; c.fill = GridBagConstraints.NONE; c.weightx = 0;
        f.add(new JLabel("Sandi:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        txtSandi = new JPasswordField(18);
        f.add(txtSandi, c);

        // Gender (JRadioButton + ButtonGroup)
        c.gridx = 0; c.gridy = 3; c.fill = GridBagConstraints.NONE; c.weightx = 0;
        f.add(new JLabel("Gender:"), c);
        c.gridx = 1;
        JPanel pGender = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rbLaki = new JRadioButton("Laki-laki", true);
        rbPerempuan = new JRadioButton("Perempuan");
        ButtonGroup g = new ButtonGroup();
        g.add(rbLaki);
        g.add(rbPerempuan);
        pGender.add(rbLaki);
        pGender.add(rbPerempuan);
        f.add(pGender, c);

        // Prodi (JComboBox)
        c.gridx = 0; c.gridy = 4;
        f.add(new JLabel("Prodi:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        cbProdi = new JComboBox<>(new String[]{"Informatika (IF)", "Sistem Informasi (SI)", "Teknik Logistik", "Manajemen Rekayasa"});
        f.add(cbProdi, c);

        // Hobi (JCheckBox: Coding, Desain, Jaringan, AI / Data, Menonton Film, Membaca)
        c.gridx = 0; c.gridy = 5; c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTHWEST;
        f.add(new JLabel("Hobi:"), c);
        c.gridx = 1;
        JPanel pHobi = new JPanel(new GridLayout(2, 3, 8, 4));
        chkCoding = new JCheckBox("Coding");
        chkDesain = new JCheckBox("Desain");
        chkJaringan = new JCheckBox("Jaringan");
        chkAI = new JCheckBox("AI / Data");
        chkFilm = new JCheckBox("Menonton Film");
        chkMembaca = new JCheckBox("Membaca");

        pHobi.add(chkCoding);
        pHobi.add(chkDesain);
        pHobi.add(chkJaringan);
        pHobi.add(chkAI);
        pHobi.add(chkFilm);
        pHobi.add(chkMembaca);
        f.add(pHobi, c);

        // Nilai + PPT Hal. 13: ItemListener untuk memantau jumlah hobi terpilih secara langsung
        lblJumlahHobi = new JLabel("Hobi dipilih: 0");
        lblJumlahHobi.setForeground(Color.BLUE);
        ItemListener hitungHobi = e -> {
            int count = 0;
            if (chkCoding.isSelected()) count++;
            if (chkDesain.isSelected()) count++;
            if (chkJaringan.isSelected()) count++;
            if (chkAI.isSelected()) count++;
            if (chkFilm.isSelected()) count++;
            if (chkMembaca.isSelected()) count++;
            lblJumlahHobi.setText("Hobi dipilih: " + count);
        };
        chkCoding.addItemListener(hitungHobi);
        chkDesain.addItemListener(hitungHobi);
        chkJaringan.addItemListener(hitungHobi);
        chkAI.addItemListener(hitungHobi);
        chkFilm.addItemListener(hitungHobi);
        chkMembaca.addItemListener(hitungHobi);

        c.gridx = 1; c.gridy = 6; c.anchor = GridBagConstraints.WEST;
        f.add(lblJumlahHobi, c);

        // Tombol Daftar (Mnemonic & ToolTip)
        c.gridx = 1; c.gridy = 7; c.anchor = GridBagConstraints.EAST;
        JButton btnDaftar = new JButton("Daftar");
        btnDaftar.setMnemonic('D');
        btnDaftar.setToolTipText("Tampilkan ringkasan pendaftaran (Alt+D)");
        btnDaftar.addActionListener(e -> prosesDaftar());
        f.add(btnDaftar, c);

        return f;
    }

    private void prosesDaftar() {
        String nama = txtNama.getText().trim();
        String email = txtEmail.getText().trim();
        char[] sandiChars = txtSandi.getPassword(); // Sesuai PPT Hal. 10: pakai char[]

        if (nama.isEmpty() || email.isEmpty() || sandiChars.length == 0) {
            JOptionPane.showMessageDialog(this, "Nama, Email, dan Sandi harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String gender = rbLaki.isSelected() ? "Laki-laki" : "Perempuan";
        String prodi = (String) cbProdi.getSelectedItem();

        List<String> hobi = new ArrayList<>();
        if (chkCoding.isSelected()) hobi.add("Coding");
        if (chkDesain.isSelected()) hobi.add("Desain");
        if (chkJaringan.isSelected()) hobi.add("Jaringan");
        if (chkAI.isSelected()) hobi.add("AI / Data");
        if (chkFilm.isSelected()) hobi.add("Menonton Film");
        if (chkMembaca.isSelected()) hobi.add("Membaca");

        String ringkasan = "Ringkasan Data Registrasi:\n"
                + "Nama   : " + nama + "\n"
                + "Email  : " + email + "\n"
                + "Sandi  : " + sandiChars.length + " karakter\n"
                + "Gender : " + gender + "\n"
                + "Prodi  : " + prodi + "\n"
                + "Hobi   : " + (hobi.isEmpty() ? "Tidak ada" : String.join(", ", hobi));

        Arrays.fill(sandiChars, '0');
        JOptionPane.showMessageDialog(this, ringkasan, "Registrasi Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== 2. EDITOR TEKS (PPT Hal. 10, 12, 15) ====================
    private JPanel buatTabEditor() {
        JPanel p = new JPanel(new BorderLayout());
        fc = new JFileChooser();

        area = new JTextArea(10, 30);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        // JPopupMenu (Klik Kanan pada JTextArea)
        JPopupMenu pop = new JPopupMenu();
        JMenuItem pPotong = new JMenuItem("Potong");
        JMenuItem pSalin = new JMenuItem("Salin");
        JMenuItem pTempel = new JMenuItem("Tempel");
        pPotong.addActionListener(e -> area.cut());
        pSalin.addActionListener(e -> area.copy());
        pTempel.addActionListener(e -> area.paste());
        pop.add(pPotong);
        pop.add(pSalin);
        pop.add(pTempel);
        area.setComponentPopupMenu(pop);

        // JToolBar + JSlider Ukuran Font
        JToolBar tool = new JToolBar();
        JButton tBaru = new JButton("Baru");
        JButton tBuka = new JButton("Buka");
        JButton tSimpan = new JButton("Simpan");
        JButton tPotong = new JButton("Potong");
        JButton tSalin = new JButton("Salin");
        JButton tTempel = new JButton("Tempel");

        tBaru.addActionListener(e -> aksiBaru());
        tBuka.addActionListener(e -> aksiBuka());
        tSimpan.addActionListener(e -> aksiSimpan());
        tPotong.addActionListener(e -> area.cut());
        tSalin.addActionListener(e -> area.copy());
        tTempel.addActionListener(e -> area.paste());

        // Nilai + PPT Hal. 12 & 13: JSlider + ChangeListener
        JLabel lblUkuran = new JLabel(" Font (14): ");
        slFont = new JSlider(10, 32, 14);
        slFont.setToolTipText("Geser untuk mengatur ukuran teks");
        slFont.addChangeListener(e -> {
            int ukuran = slFont.getValue();
            lblUkuran.setText(" Font (" + ukuran + "): ");
            area.setFont(new Font("Monospaced", Font.PLAIN, ukuran));
        });

        tool.add(tBaru);
        tool.add(tBuka);
        tool.add(tSimpan);
        tool.addSeparator();
        tool.add(tPotong);
        tool.add(tSalin);
        tool.add(tTempel);
        tool.addSeparator();
        tool.add(lblUkuran);
        tool.add(slFont);

        p.add(tool, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        return p;
    }

    private void aksiBaru() {
        area.setText("");
    }

    private void aksiBuka() {
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File f = fc.getSelectedFile();
                String isi = Files.readString(f.toPath());
                area.setText(isi);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal membaca file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void aksiSimpan() {
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File f = fc.getSelectedFile();
                Files.writeString(f.toPath(), area.getText());
                JOptionPane.showMessageDialog(this, "Tersimpan", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== 3. DATA MAHASISWA + IPK, SPINNER & UBAH ====================
    private JPanel buatTabMahasiswa() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 4, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Input Data Mahasiswa"));
        txtNim = new JTextField();
        txtMhsNama = new JTextField();
        cbMhsProdi = new JComboBox<>(new String[]{"IF", "SI", "TI", "MR"});
        // Nilai + PPT Hal. 12: JSpinner dengan SpinnerNumberModel
        spAngkatan = new JSpinner(new SpinnerNumberModel(2025, 2020, 2030, 1));
        JSpinner.NumberEditor editorAngkatan = new JSpinner.NumberEditor(spAngkatan, "#");
        spAngkatan.setEditor(editorAngkatan);
        txtIpk = new JTextField();

        form.add(new JLabel("NIM:"));
        form.add(txtNim);
        form.add(new JLabel("Nama:"));
        form.add(txtMhsNama);
        form.add(new JLabel("Prodi:"));
        form.add(cbMhsProdi);
        form.add(new JLabel("Angkatan:"));
        form.add(spAngkatan);
        form.add(new JLabel("IPK (0.00 - 4.00):"));
        form.add(txtIpk);
        form.add(new JLabel(""));
        form.add(new JLabel(""));

        model = new DefaultTableModel(new Object[]{"NIM", "Nama", "Prodi", "Angkatan", "IPK"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabel = new JTable(model);
        model.addRow(new Object[]{"01", "Budi", "IF", 2024, "3.75"});
        model.addRow(new Object[]{"02", "Siti", "SI", 2025, "3.90"});

        // JProgressBar indikator IPK mahasiswa terpilih
        barIpk = new JProgressBar(0, 400);
        barIpk.setStringPainted(true);
        barIpk.setString("Pilih baris tabel untuk melihat indikator IPK");

        tabel.getSelectionModel().addListSelectionListener(e -> {
            int baris = tabel.getSelectedRow();
            if (baris >= 0) {
                txtNim.setText(model.getValueAt(baris, 0).toString());
                txtMhsNama.setText(model.getValueAt(baris, 1).toString());
                cbMhsProdi.setSelectedItem(model.getValueAt(baris, 2).toString());
                spAngkatan.setValue(Integer.parseInt(model.getValueAt(baris, 3).toString()));
                String ipkStr = model.getValueAt(baris, 4).toString();
                txtIpk.setText(ipkStr);

                double ipkVal = Double.parseDouble(ipkStr);
                barIpk.setValue((int) (ipkVal * 100));
                barIpk.setString("Indikator IPK " + model.getValueAt(baris, 1) + ": " + ipkStr + " / 4.00");
            }
        });

        JPanel panelBawah = new JPanel(new BorderLayout(5, 5));
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton btnTambah = new JButton("Tambah");
        JButton btnUbah = new JButton("Ubah Data Terpilih");
        JButton btnHapus = new JButton("Hapus");

        btnTambah.addActionListener(e -> tambahMhs());
        btnUbah.addActionListener(e -> ubahMhs());
        btnHapus.addActionListener(e -> {
            int baris = tabel.getSelectedRow();
            if (baris >= 0) {
                int j = JOptionPane.showConfirmDialog(this, "Hapus data terpilih?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (j == JOptionPane.YES_OPTION) {
                    model.removeRow(baris);
                    resetFormMhs();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris pada tabel terlebih dahulu!");
            }
        });

        panelTombol.add(btnTambah);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);

        panelBawah.add(panelTombol, BorderLayout.NORTH);
        panelBawah.add(barIpk, BorderLayout.SOUTH);

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(tabel), BorderLayout.CENTER);
        p.add(panelBawah, BorderLayout.SOUTH);
        return p;
    }

    private Double parseValidIpk() {
        if (txtNim.getText().trim().isEmpty() || txtMhsNama.getText().trim().isEmpty() || txtIpk.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM, Nama, dan IPK wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            double ipk = Double.parseDouble(txtIpk.getText().trim());
            if (ipk < 0.0 || ipk > 4.0) {
                JOptionPane.showMessageDialog(this, "Nilai IPK harus di antara 0.00 sampai 4.00!", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return ipk;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format IPK tidak valid (gunakan titik, misal 3.65)!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void tambahMhs() {
        Double ipk = parseValidIpk();
        if (ipk == null) return;
        model.addRow(new Object[]{
                txtNim.getText().trim(),
                txtMhsNama.getText().trim(),
                cbMhsProdi.getSelectedItem(),
                spAngkatan.getValue(),
                String.format("%.2f", ipk)
        });
        resetFormMhs();
    }

    private void ubahMhs() {
        int baris = tabel.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris data yang akan diubah terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Double ipk = parseValidIpk();
        if (ipk == null) return;

        model.setValueAt(txtNim.getText().trim(), baris, 0);
        model.setValueAt(txtMhsNama.getText().trim(), baris, 1);
        model.setValueAt(cbMhsProdi.getSelectedItem(), baris, 2);
        model.setValueAt(spAngkatan.getValue(), baris, 3);
        model.setValueAt(String.format("%.2f", ipk), baris, 4);
        JOptionPane.showMessageDialog(this, "Data berhasil diubah!", "Info", JOptionPane.INFORMATION_MESSAGE);
        resetFormMhs();
    }

    private void resetFormMhs() {
        txtNim.setText("");
        txtMhsNama.setText("");
        txtIpk.setText("");
        spAngkatan.setValue(2025);
        tabel.clearSelection();
        barIpk.setValue(0);
        barIpk.setString("Pilih baris tabel untuk melihat indikator IPK");
    }

    public static void main(String[] a) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new Tugas1().setVisible(true));
    }
}