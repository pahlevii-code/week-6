import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Tugas2 extends JFrame {
    // No 1: Layout
    private JPanel panelTombol;
    private boolean modeGrid = false;

    // No 2: Form Login
    private JTextField txtUser;
    private JPasswordField txtPass;

    // No 3: Daftar Belanja
    private DefaultListModel<String> listModel;
    private JList<String> listBelanja;
    private JTextField txtBelanja;

    // No 4: Jam Digital
    private JLabel jam;

    public Tugas2() {
        super("Tugas 2 - Java Swing (No. 1 - 5)");
        setSize(540, 400);
        setLocationRelativeTo(null);

        // Nilai + PPT Hal. 6, 13, 25: DO_NOTHING_ON_CLOSE & WindowAdapter
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int j = JOptionPane.showConfirmDialog(
                        Tugas2.this,
                        "Tutup aplikasi Tugas 2?",
                        "Konfirmasi Keluar",
                        JOptionPane.YES_NO_OPTION
                );
                if (j == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        // No 5: Menu File > Keluar & Bantuan > Tentang
        setupMenu();

        JTabbedPane tab = new JTabbedPane();
        tab.addTab("1. Layout (1x3)", buatPanelNo1());
        tab.addTab("2. Form Login", buatPanelNo2());
        tab.addTab("3. Daftar Belanja", buatPanelNo3());
        tab.addTab("4. Jam Digital", buatPanelNo4());

        add(tab);
    }

    // ==================== NO 5: MENU FILE & BANTUAN ====================
    private void setupMenu() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic('F');
        JMenuItem keluar = new JMenuItem("Keluar");
        keluar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        keluar.addActionListener(e -> {
            int j = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (j == JOptionPane.YES_OPTION) System.exit(0);
        });
        file.add(keluar);

        JMenu bantuan = new JMenu("Bantuan");
        bantuan.setMnemonic('B');
        JMenuItem tentang = new JMenuItem("Tentang");
        tentang.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Aplikasi Tugas 2 - Pemrograman 1 (Java Swing)\nDepartemen Informatika - UISI",
                "Tentang Aplikasi",
                JOptionPane.INFORMATION_MESSAGE
        ));
        bantuan.add(tentang);

        bar.add(file);
        bar.add(bantuan);
        setJMenuBar(bar);
    }

    // ==================== NO 1: FLOWLAYOUT KE GRIDLAYOUT 1 x 3 ====================
    private JPanel buatPanelNo1() {
        JPanel wadah = new JPanel(new BorderLayout(10, 10));
        wadah.setBorder(BorderFactory.createTitledBorder("Ubah FlowLayout ke GridLayout (1 x 3)"));

        panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 30));
        panelTombol.add(new JButton("Tombol 1"));
        panelTombol.add(new JButton("Tombol 2"));
        panelTombol.add(new JButton("Tombol 3"));

        JButton btnUbah = new JButton("Ubah ke GridLayout 1 x 3");
        btnUbah.setMnemonic('U');
        btnUbah.setToolTipText("Ubah tata letak tombol secara dinamis (Alt+U)");
        btnUbah.addActionListener(e -> {
            if (!modeGrid) {
                panelTombol.setLayout(new GridLayout(1, 3, 10, 10));
                btnUbah.setText("Kembalikan ke FlowLayout");
            } else {
                panelTombol.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 30));
                btnUbah.setText("Ubah ke GridLayout 1 x 3");
            }
            modeGrid = !modeGrid;
            panelTombol.revalidate();
            panelTombol.repaint();
        });

        wadah.add(panelTombol, BorderLayout.CENTER);
        wadah.add(btnUbah, BorderLayout.SOUTH);
        return wadah;
    }

    // ==================== NO 2: FORM LOGIN DENGAN GRIDBAGLAYOUT ====================
    private JPanel buatPanelNo2() {
        JPanel f = new JPanel(new GridBagLayout());
        f.setBorder(BorderFactory.createTitledBorder("Login Sistem"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        f.add(new JLabel("Username:"), c);

        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        txtUser = new JTextField(15);
        f.add(txtUser, c);

        c.gridx = 0; c.gridy = 1; c.fill = GridBagConstraints.NONE; c.weightx = 0;
        f.add(new JLabel("Password:"), c);

        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        txtPass = new JPasswordField(15);
        // Tekan Enter di kolom password langsung menjalankan login
        txtPass.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) cekLogin();
            }
        });
        f.add(txtPass, c);

        c.gridx = 1; c.gridy = 2; c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.EAST;
        JButton btnMasuk = new JButton("Masuk");
        btnMasuk.setMnemonic('M');
        btnMasuk.addActionListener(e -> cekLogin());
        f.add(btnMasuk, c);

        return f;
    }

    private void cekLogin() {
        String u = txtUser.getText().trim();
        char[] p = txtPass.getPassword(); // Sesuai PPT Hal. 10: pakai char[]

        if (u.isEmpty() || p.length == 0) {
            JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Selamat datang, " + u + "!", "Login Berhasil", JOptionPane.INFORMATION_MESSAGE);
        }
        Arrays.fill(p, '0');
    }

    // ==================== NO 3: DAFTAR BELANJA ====================
    private JPanel buatPanelNo3() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel atas = new JPanel(new BorderLayout(5, 5));
        txtBelanja = new JTextField();
        txtBelanja.setToolTipText("Ketik nama barang lalu klik Tambah atau tekan Enter");
        // Nilai + PPT Hal. 25: KeyAdapter
        txtBelanja.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) tambahBelanja();
            }
        });

        JButton btnTambah = new JButton("Tambah");
        btnTambah.setMnemonic('T');
        btnTambah.addActionListener(e -> tambahBelanja());

        atas.add(new JLabel("Item:"), BorderLayout.WEST);
        atas.add(txtBelanja, BorderLayout.CENTER);
        atas.add(btnTambah, BorderLayout.EAST);

        listModel = new DefaultListModel<>();
        listModel.addElement("Beras");
        listModel.addElement("Telur");
        listBelanja = new JList<>(listModel);
        listBelanja.setToolTipText("Klik ganda pada item untuk mengubah namanya");

        // Nilai + PPT Hal. 14 & 25: MouseAdapter (klik ganda item untuk edit)
        listBelanja.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int idx = listBelanja.getSelectedIndex();
                    if (idx >= 0) {
                        String baru = JOptionPane.showInputDialog(Tugas2.this, "Ubah nama item:", listModel.get(idx));
                        if (baru != null && !baru.trim().isEmpty()) {
                            listModel.set(idx, baru.trim());
                        }
                    }
                }
            }
        });

        JButton btnHapus = new JButton("Hapus Terpilih");
        btnHapus.setMnemonic('H');
        btnHapus.addActionListener(e -> {
            int idx = listBelanja.getSelectedIndex();
            if (idx >= 0) {
                listModel.remove(idx);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih item pada daftar terlebih dahulu!", "Info", JOptionPane.WARNING_MESSAGE);
            }
        });

        p.add(atas, BorderLayout.NORTH);
        p.add(new JScrollPane(listBelanja), BorderLayout.CENTER);
        p.add(btnHapus, BorderLayout.SOUTH);
        return p;
    }

    private void tambahBelanja() {
        String item = txtBelanja.getText().trim();
        if (item.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama barang tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        listModel.addElement(item);
        txtBelanja.setText("");
        txtBelanja.requestFocus();
    }

    // ==================== NO 4: JAM DIGITAL DENGAN TIMER (PPT Hal. 18) ====================
    private JPanel buatPanelNo4() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(230, 244, 252)); // Sesuai contoh warna PPT Hal. 19

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        jam = new JLabel(LocalTime.now().format(fmt));
        jam.setFont(new Font("Arial", Font.BOLD, 36));
        jam.setForeground(Color.BLUE);

        // Sesuai kode PPT Hal. 18: javax.swing.Timer dengan LocalTime.now()
        Timer t = new Timer(1000, e -> jam.setText(LocalTime.now().format(fmt)));
        t.start();

        p.add(jam);
        return p;
    }

    public static void main(String[] a) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new Tugas2().setVisible(true));
    }
}