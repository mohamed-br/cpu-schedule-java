package com.company;

import javax.naming.Name;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;

import static javax.swing.JOptionPane.showMessageDialog;

public class GUI implements ActionListener {
    JTable table;
    JComboBox comb,combColor;
    String name, color, methode;
    JTextField text1, text2, text3;
    JButton enter, add, startSimulation;
    JTextField Ptext1, Ptext2, Ptext3, Ptext4, Ptext5, Ptext6;
    JLabel Plable1, Plable2, Plable3, Plable4, Plable5, Plable6, Plable7;
    int number_processes, time_quantum, context_switching, arrival_Time, burst_Time, priority_number;
    NewFrame frame1 = new NewFrame("Input frame", 500, 400);
    NewFrame Process_frame = new NewFrame("Process data", 600, 500);
    NewFrame simulation_Frame = new NewFrame("Simulation frame", 2000, 1000);
    ArrayList<process> solved = new ArrayList<>();
    ArrayList<process_work> solv = new ArrayList<>();

    GUI() {
        frame1.setColor(4);
        JLabel label1, label2, label3;
        String[] methods = {"SJf", "SRTF", "Priority methode", "AGAT"};
        comb = new JComboBox(methods);
        text1 = new JTextField("1");
        text1.setBounds(180, 50, 150, 30);
        text2 = new JTextField("1");
        text2.setBounds(180, 100, 150, 30);
        text3 = new JTextField("1");
        text3.setBounds(180, 150, 150, 30);
        label1 = new JLabel("Process number");
        label1.setBounds(70, 50, 150, 30);
        label2 = new JLabel("Time Quantum");
        label2.setBounds(70, 100, 150, 30);
        label3 = new JLabel("Context switching ");
        label3.setBounds(70, 150, 150, 30);
        Plable7 = new JLabel("CPU Scheduling");
        Plable7.setBounds(70, 200, 120, 30);
        comb.setBounds(180, 200, 120, 30);
        enter = new JButton("Enter");
        enter.addActionListener(this);
        enter.setBounds(200, 250, 100, 30);
        frame1.AddObject(text1);
        frame1.AddObject(text2);
        frame1.AddObject(text3);
        frame1.AddObject(label1);
        frame1.AddObject(label2);
        frame1.AddObject(label3);
        frame1.AddObject(enter);
        frame1.AddObject(Plable7);
        frame1.AddObject(comb);
        frame1.SetV_L();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == enter) {
            methode = String.valueOf(comb.getItemAt(comb.getSelectedIndex()));
            frame1.disposeFrame();
            String []comLabels={"red","blue","gray","green","black","cyan","magenta","yellow"};;
            number_processes = Integer.parseInt(text1.getText());
            time_quantum = Integer.parseInt(text2.getText());
            context_switching = Integer.parseInt(text3.getText());
            Process_frame.setColor(1);
            Ptext1 = new JTextField();
            Ptext1.setBounds(180, 50, 150, 30);
            combColor = new JComboBox(comLabels);
            combColor.setBounds(180, 100, 150, 30);
            Ptext3 = new JTextField();
            Ptext3.setBounds(180, 150, 50, 30);
            Ptext4 = new JTextField();
            Ptext4.setBounds(180, 200, 50, 30);
            Ptext5 = new JTextField();
            Ptext5.setBounds(180, 250, 50, 30);
            Plable1 = new JLabel("Name");
            Plable1.setBounds(80, 50, 100, 30);
            Plable2 = new JLabel("Color");
            Plable2.setBounds(80, 100, 100, 30);
            Plable3 = new JLabel("Arrival time");
            Plable3.setBounds(80, 150, 100, 30);
            Plable4 = new JLabel("Burst time");
            Plable4.setBounds(80, 200, 100, 30);
            Plable5 = new JLabel("Priority number");
            Plable5.setBounds(80, 250, 120, 30);
            add = new JButton("Add");
            add.setBounds(150, 350, 140, 30);
            add.addActionListener(this);
            startSimulation = new JButton("Start simulation");
            startSimulation.setBounds(310, 350, 140, 30);
            startSimulation.addActionListener(this);
            Process_frame.AddObject(Ptext1);
            Process_frame.AddObject(combColor);
            Process_frame.AddObject(Ptext3);
            Process_frame.AddObject(Ptext4);
            Process_frame.AddObject(Ptext5);
            Process_frame.AddObject(Plable1);
            Process_frame.AddObject(Plable2);
            Process_frame.AddObject(Plable3);
            Process_frame.AddObject(Plable4);
            Process_frame.AddObject(Plable5);
            if (methode.equals("AGAT")) {
                Ptext6 = new JTextField();
                Ptext6.setBounds(180, 300, 50, 30);
                Plable6 = new JLabel("Quantum");
                Plable6.setBounds(80, 300, 120, 30);
                Process_frame.AddObject(Ptext6);
                Process_frame.AddObject(Plable6);
            }
            Process_frame.AddObject(add);
            Process_frame.AddObject(startSimulation);
            Process_frame.SetV_L();
        } else if (e.getSource() == add) {
            if (methode.equals("AGAT")) {
                Main.Totalprocesses.add(new process(Ptext1.getText(), String.valueOf(combColor.getItemAt(combColor.getSelectedIndex())), Integer.parseInt(Ptext3.getText()), Integer.parseInt(Ptext4.getText()), Integer.parseInt(Ptext5.getText()), Integer.parseInt(Ptext6.getText())));
                Ptext6.setText("");
            } else {
                Main.Totalprocesses.add(new process(Ptext1.getText(), String.valueOf(combColor.getItemAt(combColor.getSelectedIndex())), Integer.parseInt(Ptext3.getText()), Integer.parseInt(Ptext4.getText()), Integer.parseInt(Ptext5.getText()), time_quantum));
            }
            Ptext1.setText("");
            Ptext3.setText("");
            Ptext4.setText("");
            Ptext5.setText("");
            showMessageDialog(Process_frame.getFrame(), "Process added");
        } else if (e.getSource() == startSimulation) {
            NewFrame dum = new NewFrame("CPU Scheduling Graph", 0, 0);
            double AGAT_Wait=0,AGAT_turn=0;
            dum.setColor(5);
            switch (methode) {
                case "SJf":
                    sjf_method sjf = new sjf_method(Main.Totalprocesses);
                    solved = sjf.solve();
                    print.print(solved);
                    break;
                case "SRTF":
                    srtf srt = new srtf(Main.Totalprocesses);
                    solved = srt.solve();
                    print.print(solved);
                    break;
                case "Priority methode":
                    priority_method priority = new priority_method(Main.Totalprocesses, context_switching);
                    solved = priority.solve();
                    print.print(solved);
                    break;
                case "AGAT":
                    AGAT agat = new AGAT(Main.Totalprocesses);
                    solv = agat.getProcesses_work();
                    AGAT_turn=agat.average_Turnaround_Time();
                    AGAT_Wait=agat.average_Waiting_Time();
                    break;
            }
            Process_frame.disposeFrame();
            JLabel Slable1, Slable2;
            JLabel avgLabel,avgLabe2;
            dum.setBounds(10, 60, 1450, 600);
            dum.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            Slable1 = new JLabel("CPU Scheduling Graph");
            Slable1.setForeground(Color.RED);
            Slable1.setFont(new Font("Times new roman", Font.ROMAN_BASELINE, 20));
            Slable1.setBounds(10, 20, 200, 30);
            Slable2 = new JLabel("Processes information");
            Slable2.setForeground(Color.RED);
            Slable2.setFont(new Font("Times new roman", Font.ROMAN_BASELINE, 20));
            Slable2.setBounds(1500, 20, 200, 30);
            if (methode.equals("AGAT")) {
                AGAT_tableDesign();
                AGAT_Design(dum);
                avgLabel=new JLabel("Average Waiting time : "+String.valueOf(AGAT_Wait));
                avgLabe2=new JLabel("Average Turn time : "+String.valueOf(AGAT_turn));
            } else {
                jTableDesign();
                jFramedesign(dum);
                avgLabel=new JLabel("Average Waiting time :"+String.valueOf(print.getAvg_wait()));
                avgLabe2=new JLabel("Average Turn time :"+String.valueOf(print.getAvg_trun()));
            }
            avgLabel.setBounds(10, 680, 400, 30);
            avgLabel.setForeground(Color.green);
            avgLabe2.setBounds(10,720 , 400, 30);
            avgLabe2.setForeground(Color.green);
            avgLabel.setFont(new Font("Times new roman", Font.ROMAN_BASELINE, 20));
            avgLabe2.setFont(new Font("Times new roman", Font.ROMAN_BASELINE, 20));
            simulation_Frame.setColor(2);
            simulation_Frame.AddObject(Slable2);
            simulation_Frame.AddObject(Slable1);
            simulation_Frame.AddObject(table);
            simulation_Frame.AddObject(dum);
            simulation_Frame.AddObject(avgLabel);
            simulation_Frame.AddObject(avgLabe2);
            simulation_Frame.SetV_L();
            dum.SetV_L();
        }
    }
    public void AGAT_tableDesign(){
        String[] headers = {"Name", "Color","Start","End"};
        String[][] data = new String[solv.size()+1][5];
        data[0] = headers;
        for (int i = 0; i < solv.size(); i++) {
            data[i + 1] = solv.get(i).toString().split(" ");
        }
        table = new JTable(data, headers);
        table.setBounds(1500, 60, 400, 600);
    }
    public void jTableDesign() {
        String[] headers = {"Name", "Color", "Priority","Waiting Time","Turn Around"};
        String[][] data = new String[solved.size()+1][5];
        data[0] = headers;
        for (int i = 0; i < solved.size(); i++) {
            data[i + 1] = solved.get(i).toString().split(" ");
        }
        table = new JTable(data, headers);
        table.setBounds(1500, 60, 400, 600);
    }

    public void AGAT_Design(JComponent jFram) {
        JButton butt;
        JLabel labe;
        int f = 120, row = 0;
        ArrayList<String> process_posi = new ArrayList<>();
        for (int i=0;i<solv.size();i++){
            String process_name = solv.get(i).getName();
            if (process_posi.indexOf(process_name) < 0) {
                process_posi.add(process_name);
                row = process_posi.size();
                labe=new JLabel(process_name);
                labe.setForeground(Color.WHITE);
                labe.setBounds(10,(30*row)+10,100,30);
                labe.setFont(new Font("Times new roman", Font.ROMAN_BASELINE, 20));
                jFram.add(labe);
            } else {
                row = process_posi.indexOf(process_name) + 1;
            }
        }
        for (int i = 0; i < solv.size(); i++) {
            Color ci = Color.white;
            switch (solv.get(i).getColor().toLowerCase(Locale.ROOT)) {
                case "red":
                    ci = Color.RED;
                    break;
                case "blue":
                    ci = Color.blue;
                    break;
                case "gray;":
                    ci = Color.GRAY;
                    break;
                case "green":
                    ci = Color.green;
                    break;
                case "black":
                    ci = Color.black;
                    break;
                case "cyan":
                    ci = Color.CYAN;
                    break;
                case "magenta":
                    ci = Color.MAGENTA;
                    break;
                case "yellow":
                    ci = Color.yellow;
                    break;
            }
            if (i == 0) {
                butt=new JButton();
                butt.setBackground(ci);
                butt.setBounds(120,((1+process_posi.indexOf(solv.get(i).getName()))*30)+10,10+(solv.get(i).getEnd()-solv.get(i).getStart()),30);
                f=f+10+(solv.get(i).getEnd()-solv.get(i).getStart());
            } else {
                butt=new JButton();
                butt.setBackground(ci);
                butt.setBounds(f+10,((1+process_posi.indexOf(solv.get(i).getName()))*30)+10,10+(solv.get(i).getEnd()-solv.get(i).getStart()),30);
                f=f+10+(solv.get(i).getEnd()-solv.get(i).getStart());
            }
            jFram.add(butt);
        }
    }

    public void jFramedesign(JComponent jFram) {
        int f = 120;
        for (int i = 0; i < solved.size(); i++) {
            JButton butt;
            JLabel labe;
            Color ci = Color.white;
            switch (solved.get(i).getcolor().toLowerCase(Locale.ROOT)) {
                case "red":
                    ci = Color.RED;
                    break;
                case "blue":
                    ci = Color.blue;
                    break;
                case "gray;":
                    ci = Color.GRAY;
                    break;
                case "green":
                    ci = Color.green;
                    break;
                case "black":
                    ci = Color.black;
                    break;
                case "cyan":
                    ci = Color.CYAN;
                    break;
                case "magenta":
                    ci = Color.MAGENTA;
                    break;
                case "yellow":
                    ci = Color.yellow;
                    break;
            }
            if (i == 0) {
                f = 40 + f + solved.get(i).getFake_burst() - 10;
                butt = new JButton();
                labe = new JLabel(solved.get(i).getname());
                labe.setForeground(Color.WHITE);
                labe.setFont(new Font("Times new roman", Font.ROMAN_BASELINE, 20));
                labe.setBounds(10, 30, 100, 30);
                butt.setBackground(ci);
                butt.setBounds(120, 30, 40 + solved.get(i).getFake_burst(), 30);
                //System.out.println(f);
                //System.out.println(solved.get(i).getFake_burst());
            } else {
                f = 40 + f + solved.get(i).getFake_burst();
                labe = new JLabel(solved.get(i).getname());
                labe.setBounds(10, ((i + 1) * 30) + ((i + 1) * 10), 100, 30);
                labe.setForeground(Color.WHITE);
                labe.setFont(new Font("Times new roman", Font.ROMAN_BASELINE, 20));
                butt = new JButton();
                butt.setBackground(ci);
                butt.setBounds(f, ((i + 1) * 30) + ((i + 1) * 10), 40 + solved.get(i).getFake_burst(), 30);
                //System.out.println(f);
                //System.out.println(solved.get(i).getFake_burst());
            }
            jFram.add(butt);
            jFram.add(labe);
        }
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
    }
}

class NewFrame extends JComponent {
    JFrame frame;

    public NewFrame(String frameName) {
        frame = new JFrame(frameName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
    }

    public NewFrame(String frameName, int width, int length) {
        frame = new JFrame(frameName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, length);
    }

    public void AddObject(Component obj) {
        frame.add(obj);
    }

    public void SetV_L() {
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public void setColor(int color) {
        switch (color) {
            case 1:
                frame.getContentPane().setBackground(Color.CYAN);
                break;
            case 2:
                frame.getContentPane().setBackground(Color.darkGray);
                break;
            case 3:
                frame.getContentPane().setBackground(Color.RED);
                break;
            case 4:
                frame.getContentPane().setBackground(Color.green);
                break;
            case 5:
                frame.getContentPane().setBackground(Color.white);
        }
    }

    public void disposeFrame() {
        frame.dispose();
    }

    public JFrame getFrame() {
        return frame;
    }
}
//P1 17 0 4 4
//P2 6 3 9 3
//P3 10 4 3 5
//P4 4 29 8 2