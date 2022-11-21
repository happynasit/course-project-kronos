package UI;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class TimerView extends JFrame
{
    private JPanel topPanel;
    String number1;
    String number2;
    // Custom Components
    // RGB Codes for skyBlue. Used as Background.
    private final Color skyBlue = new Color(0, 50, 200);

    // Used for displaying the timer values.
    private final Font timerStyle = new Font("MonoAlphabet", Font.BOLD, 140);

    // RGB codes for cardinalRed. Used as Background for buttons.
    private final Color cardinalRed = new Color(189, 32, 49);

    // Used in the buttons in the Pomodoro Timer section.
    private final Font formBTStyles = new Font("Existence", Font.BOLD, 20);

    // Used in the buttons in the Pomodoro Timer section.
    private final Font delayLabelStyles = new Font("Existence", Font.ITALIC, 20);

    // RGB code for indiaGreen. Used as background when the timer is paused.
    private final Color indiaGreen = new Color(19, 136, 8);

    private static final int ORIGINAL_COUNTDOWN_MINUTES = 25;
    private static final int ORIGINAL_COUNTDOWN_SECONDS = 0;
    private static final int ORIGINAL_SHORTBREAK_MINUTES = 5;
    private static final int ORIGINAL_SHORTBREAK_SECONDS = 0;
    private static final int ORIGINAL_LONGBREAK_MINUTES = 15;
    private static final int ORIGINAL_LONGBREAK_SECONDS = 0;
    private static final int TOTAL_DELAY_TIME = 30;
    private static final int INTERVAL = 1000; // Iteration interval for the Timers.
    private static final int ONE_POMODORO_CYCLE = 8; // No of rounds in a single Pomodoro cycle.

    private JPanel timerPanel;

    private JLabel minuteLabel;
    private JLabel separator;
    private JLabel secondLabel;
    private JLabel delayRemainingLabel;

    private JButton startPauseBT;
    private JButton stopBT;
    private JButton continueBT;

    private Icon startIcon;
    private Icon pauseIcon;
    private Icon stopIcon;
    private Icon skipIcon;

    private Timer countDown;
    private Timer workTimer;
    private Timer restTimer;
    private Timer delayTimer;
    private int secondsRemaining;
    private int minutesRemaining;
    private int delayRemaining;
    private int roundsCompleted; // No. of Pomodoro rounds completed.

    //private static final String workTime = Timertomato.getWorkTime();
    //static String[] work = workTime.split("\\:");

    //private static final int workTime_minutes = Integer.parseInt(work[0]);
    //private static final int workTime_seconds = Integer.parseInt(work[1]);

    //private static final String restTime = Timertomato.getRestTime();
    //static String[] rest = getRestTime().split("\\:");
    //private static final int restTimer_minutes = Integer.parseInt(rest[0]);
    //private static final int restTimer_seconds = Integer.parseInt(rest[1]);


    public TimerView()
    {
        setTitle("Pomodoro Timer");

        // Setting up the Cloud Look and Feel of the GUI application
        try
        {
            for(LookAndFeelInfo info: UIManager.getInstalledLookAndFeels())
            {
                if("Cloud".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            // To disable 'Space' button to act as a click for buttons if it has focus.
            InputMap im = (InputMap)UIManager.get("Button.focusInputMap");
            im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
            im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
        }

        catch(ClassNotFoundException | InstantiationException | IllegalAccessException |
              UnsupportedLookAndFeelException e)
        {
            // If Cloud is not available, you can set the GUI to default look and feel.
        }

        topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(900, 600));
        topPanel.setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.CENTER);
        roundsCompleted = 0;

        topPanel.add(addMainTimer());
    }


    private JPanel addMainTimer()
    {
        timerPanel = new JPanel();
        timerPanel.setBackground(skyBlue);
        timerPanel.setLayout(new MigLayout("insets 115 0 0 0", "", "[][]0[]"));

        minuteLabel = new JLabel(String.format("%02d", ORIGINAL_COUNTDOWN_MINUTES));
        minuteLabel.setForeground(Color.white);
        minuteLabel.setFont(timerStyle);
        timerPanel.add(minuteLabel, "split 3, gapright 20, gaptop 20, pushx, spanx, alignx center, height 145!");

        separator = new JLabel(":");
        separator.setForeground(Color.white);
        separator.setFont(timerStyle);
        timerPanel.add(separator, "alignx center, gapright 20, height 145!");

        secondLabel = new JLabel(String.format("%02d", ORIGINAL_COUNTDOWN_SECONDS));
        secondLabel.setForeground(Color.white);
        secondLabel.setFont(timerStyle);
        timerPanel.add(secondLabel, "alignx center, height 145!, wrap");

        startIcon = new ImageIcon("src/Play.gif");
        pauseIcon = new ImageIcon("src/Pause.png");
        startPauseBT = new JButton("Start", startIcon);
        startPauseBT.setContentAreaFilled(false);
        startPauseBT.setBackground(indiaGreen);
        startPauseBT.setActionCommand("Begin");
        startPauseBT.setForeground(Color.white);
        startPauseBT.setFont(formBTStyles);
        timerPanel.add(startPauseBT, "gaptop 20, alignx center, split 3, spanx, pushx");

        skipIcon = new ImageIcon("src/Skip.png");
        continueBT = new JButton("Skip", skipIcon);
        continueBT.setBackground(cardinalRed);
        continueBT.setContentAreaFilled(false);
        continueBT.setForeground(Color.white);
        continueBT.setFont(formBTStyles);
        continueBT.setVisible(false);
        timerPanel.add(continueBT, "alignx center, hidemode 0, gapleft 30, gapright 30");

        stopIcon = new ImageIcon("src/Stop.png");
        stopBT = new JButton("Stop", stopIcon);
        stopBT.setContentAreaFilled(false);
        stopBT.setBackground(cardinalRed);
        stopBT.setActionCommand("Stop");
        stopBT.setForeground(Color.white);
        stopBT.setFont(formBTStyles);
        timerPanel.add(stopBT, "gaptop 5, alignx center, split 3, spanx, pushx");

        delayRemainingLabel = new JLabel("Resumes in " + TOTAL_DELAY_TIME + " seconds");
        delayRemainingLabel.setForeground(Color.white);
        delayRemainingLabel.setVisible(false);
        delayRemainingLabel.setFont(delayLabelStyles);
        timerPanel.add(delayRemainingLabel, "alignx center");


        JTextField text3 = new JTextField(100);
        text3.setPreferredSize(new Dimension(1,50));
        timerPanel.add(text3);
        JTextField text4 = new JTextField(100);
        text4.setPreferredSize(new Dimension(1,50));
        timerPanel.add(text4);
        JButton button1 = new JButton("Setting");
        button1.setFont(new Font("", Font.PLAIN, 50));
        timerPanel.add(button1);

        button1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // set time
                number1 = text3.getText();
                number2 = text4.getText();
                int i1 = Integer.parseInt(number1);
                int i2 = Integer.parseInt(number2);
                minuteLabel.setText(String.format("%02d", i1));
                secondLabel.setText(String.format("%02d", i2));
            }

        });

        startPauseBT.addActionListener((ActionEvent event) -> {
            switch (event.getActionCommand()) {
                case "Pause":
                    countDown.stop();
                    countDownPaused();
                    break;
                case "Start":
                case "Begin":
                    countDown.start();
                    stopBT.setEnabled(true);
                    startPauseBT.setIcon(pauseIcon);
                    startPauseBT.setActionCommand("Pause");
                    break;
                default:
                    break;
            }
        });

        continueBT.addActionListener((ActionEvent event) -> {
            if(event.getActionCommand().equals("SkipRestTimer"))
            {
                restTimer.stop();
                roundsCompleted++;
            }
            else if(event.getActionCommand().equals("SkipWorkTimer"))
            {
                workTimer.stop();
                roundsCompleted = 0; // Timer is Reset.
            }

            startPauseBT.setVisible(true);
            continueBT.setVisible(false);
            stopBT.setVisible(true);
            runMainTimer();
        });

        stopBT.addActionListener((ActionEvent event) -> {
            countDown.stop();
            secondsRemaining = ORIGINAL_COUNTDOWN_SECONDS;
            minutesRemaining = ORIGINAL_COUNTDOWN_MINUTES;
            minuteLabel.setText(String.format("%02d", ORIGINAL_COUNTDOWN_MINUTES));
            secondLabel.setText(String.format("%02d", ORIGINAL_COUNTDOWN_SECONDS));
            stopBT.setEnabled(false);
            roundsCompleted = 0; // Timer is Reset.
            startPauseBT.setIcon(startIcon);
            startPauseBT.setActionCommand("Start");
        });

        runMainTimer();

        return timerPanel;
    }

    private void runMainTimer()
    {
        //System.out.println("Start Main " + String.format("%d", roundsCompleted));
        minutesRemaining = ORIGINAL_COUNTDOWN_MINUTES;
        secondsRemaining = ORIGINAL_COUNTDOWN_SECONDS;

        minuteLabel.setText(String.format("%02d", ORIGINAL_COUNTDOWN_MINUTES));
        secondLabel.setText(String.format("%02d", ORIGINAL_COUNTDOWN_SECONDS));

        if(roundsCompleted == ONE_POMODORO_CYCLE)
        {
            //System.out.println("Stop Long " + roundsCompleted);
            workTimer.stop();
            roundsCompleted = 0; // Timer is Reset.
        }

        else if(roundsCompleted > 0 && roundsCompleted % 2 == 0)
        {
            //System.out.println("Stop Short " + roundsCompleted);
            restTimer.stop();
        }
        // else
        {
            System.out.println("Don't Stop Main " + roundsCompleted);
        }

        countDown = new Timer(INTERVAL, (ActionEvent event) -> {
            if(secondsRemaining == ORIGINAL_COUNTDOWN_SECONDS)
            {
                if(minutesRemaining == 0)
                {
                    //startPauseBT.setText("Begin");
                    //startPauseBT.setActionCommand("Start");
                    //mainCompleted = false;
                    roundsCompleted++;

                    // Selection of which break timer to run.
                    if(roundsCompleted == ONE_POMODORO_CYCLE)
                    {
                        runWorkTimer();
                    }
                    else if(roundsCompleted > 0 && roundsCompleted % 2 == 0)
                    {
                        runRestTimer();
                    }
                }
                else
                {
                    minutesRemaining -= 1;
                    secondsRemaining = 59;
                    minuteLabel.setText(String.format("%02d", minutesRemaining));
                    secondLabel.setText(String.format("%02d", secondsRemaining));
                }
            }
            else
            {
                if(secondsRemaining > ORIGINAL_COUNTDOWN_SECONDS)
                {
                    secondsRemaining -= 1;
                    secondLabel.setText(String.format("%02d", secondsRemaining));
                }
            }
        });

        countDown.start();
    }

    private void runRestTimer()
    {
        //System.out.println("Start Short " + String.format("%d", roundsCompleted));
        minutesRemaining = ORIGINAL_SHORTBREAK_MINUTES;
        secondsRemaining = ORIGINAL_SHORTBREAK_SECONDS;
        stopBT.setVisible(false);
        startPauseBT.setVisible(false);
        continueBT.setVisible(false);
        continueBT.setActionCommand("SkipShortTimer");
        countDown.stop();

        minuteLabel.setText(String.format("%02d", ORIGINAL_SHORTBREAK_MINUTES));
        secondLabel.setText(String.format("%02d", ORIGINAL_SHORTBREAK_SECONDS));

        restTimer = new Timer(INTERVAL, (ActionEvent event) -> {
            if(secondsRemaining == ORIGINAL_SHORTBREAK_SECONDS)
            {
                if(minutesRemaining == 0)
                {
                    continueBT.setVisible(false);
                    stopBT.setVisible(true);
                    startPauseBT.setVisible(true);
                    //roundsCompleted++;
                    runMainTimer();
                }
                else
                {
                    minutesRemaining -= 1;
                    secondsRemaining = 59;
                    minuteLabel.setText(String.format("%02d", minutesRemaining));
                    secondLabel.setText(String.format("%02d", secondsRemaining));
                }
            }
            else
            {
                secondsRemaining -= 1;
                secondLabel.setText(String.format("%02d", secondsRemaining));
            }
        });

        restTimer.start();
    }

    private void runWorkTimer()
    {
        //System.out.println("Start Long " + String.format("%d", roundsCompleted));
        minutesRemaining = ORIGINAL_LONGBREAK_MINUTES;
        secondsRemaining = ORIGINAL_LONGBREAK_SECONDS;
        stopBT.setVisible(true);
        startPauseBT.setVisible(true);
        continueBT.setVisible(true);
        continueBT.setActionCommand("SkipLongTimer");
        countDown.stop();

        minuteLabel.setText(String.format("%02d", ORIGINAL_LONGBREAK_MINUTES));
        secondLabel.setText(String.format("%02d", ORIGINAL_LONGBREAK_SECONDS));

        workTimer = new Timer(INTERVAL, (ActionEvent event) -> {
            if(secondsRemaining == 0)
            {
                if(minutesRemaining == 0)
                {
                    continueBT.setVisible(false);
                    stopBT.setVisible(true);
                    startPauseBT.setVisible(true);
                    //roundsCompleted++;
                    runMainTimer();
                }
                else
                {
                    minutesRemaining -= 1;
                    secondsRemaining = 59;
                    minuteLabel.setText(String.format("%02d", minutesRemaining));
                    secondLabel.setText(String.format("%02d", secondsRemaining));
                }
            }
            else
            {
                secondsRemaining -= 1;
                secondLabel.setText(String.format("%02d", secondsRemaining));
            }
        });

        workTimer.start();
    }

    private void countDownPaused()
    {
        delayRemaining = TOTAL_DELAY_TIME;
        delayRemainingLabel.setVisible(true);
        startPauseBT.setVisible(false);
        stopBT.setVisible(false);
        countDown.stop();

        delayTimer = new Timer(INTERVAL, (ActionEvent event) -> {
            if(delayRemaining > 0)
            {
                delayRemainingLabel.setText("Resumes in " + delayRemaining + " seconds");
                delayRemaining--;
            }
            else
            {
                delayTimer.stop();
                delayRemainingLabel.setVisible(false);
                startPauseBT.setIcon(pauseIcon);
                delayRemainingLabel.setText("Resumes in " + TOTAL_DELAY_TIME + " seconds");
                countDown.start();
                stopBT.setVisible(true);
                startPauseBT.setVisible(true);
                startPauseBT.requestFocusInWindow();
            }
        });


        delayTimer.start();
    }

    private static void runGUI()
    {
        TimerView mainFrame = new TimerView();
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(TimerView::runGUI);
    }






}
