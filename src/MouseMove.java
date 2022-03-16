import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.*;

/*
 自动平滑移动鼠标至随机位置 可拖动打断 调节延时
         冯雪原 2022.1
*/
public class MouseMove implements Runnable {
    //延时设置 单位:秒
    private int delay=10;

    private Robot robot;
    private boolean isStop = false;
    private ControllerFrame frame;
    public MouseMove() {
        try {
            frame = new ControllerFrame("MouseMove          --fxy");
            frame.setVisible(true);

            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    //平滑移动  x1 y1原位置    x2 y2新位置   t总耗时   n总步数
    public void MouseGlide(int x1, int y1, int x2, int y2, int t, int n) {
        try {
            double dx = (x2 - x1) / ((double) n);
            double dy = (y2 - y1) / ((double) n);
            double dt = t / ((double) n);

            for (int step = 1; step <= n; step++) {
                Point mousepoint=MouseInfo.getPointerInfo().getLocation();
                Thread.sleep((int) dt);
                Point mousepoint1=MouseInfo.getPointerInfo().getLocation();
                //拖动打断实现
                if(mousepoint.x==mousepoint1.x && mousepoint.y==mousepoint1.y) {
                    robot.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
                }else{
                    break;
                }
            }
            //增加100ms延时 避免停止时不容易点到stop按钮
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int x;
        int y;
        Random random = new Random();
        while (!isStop) {
            //随机生成坐标。
            x = random.nextInt(500);
            y = random.nextInt(500);
            Point mousepoint = MouseInfo.getPointerInfo().getLocation();
            frame.setLabelText("Moving...");
            MouseGlide(mousepoint.x,mousepoint.y,x,y,1500,200);
            //delay1 避免调节延时的时候延时变化
            int delay1=delay;
            for(int i=0;i<(delay1*1000)/16;i++) {
                if(isStop){
                    break;
                }
                double time=delay1-0.016*i;
                frame.setLabelText("Waiting... "+new DecimalFormat("0.00").format(time)+" s");
                Point mousepointbefore=MouseInfo.getPointerInfo().getLocation();
                robot.delay(16);
                Point mousepointafter=MouseInfo.getPointerInfo().getLocation();
                //在waiting时 鼠标位置改变 延时初始化
                if(mousepointbefore.x!=mousepointafter.x || mousepointbefore.y!=mousepointafter.y) {
                    delay1=delay;
                    i=-1;
                }
            }
        }

    }

    private class ControllerFrame extends JFrame {
        private static final long serialVersionUID = 1L;


        private JButton close = null;
        private JLabel label=new JLabel("");
        private JLabel timelabel1=new JLabel("延时(秒):");
        private JButton timebutton1 = new JButton("减小");
        private JLabel timelabel2=new JLabel(String.valueOf(delay));
        private JButton timebutton2 = new JButton("增加");
        private JButton timebutton3 = new JButton("30s");
        private JButton timebutton4 = new JButton("60s");
        private JButton timebutton5 = new JButton("120s");
        private JButton timebutton6 = new JButton("250s");
        private JButton timebutton7 = new JButton("450s");
        private JButton timebutton8 = new JButton("950s");
        public ControllerFrame(String title) {
            this();
            setTitle(title);
        }
        public void setLabelText(String text){
            this.label.setText(text);
        }
        public ControllerFrame() {
            ImageIcon icon = new ImageIcon(MouseMove.class.getResource("1.png"));
            close=new JButton(icon);
            this.setIconImage( new ImageIcon(MouseMove.class.getResource("2.png")).getImage());
            setLayout(new FlowLayout(FlowLayout.LEADING));
            setSize(377, 525);
            setResizable(false);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            Dimension preferredSize = new Dimension(350,360);
            Font font = new Font("", 1, 70);

            //设置button 大小，文字等属性
            close.setPreferredSize(preferredSize);
            label.setFont(new Font("", 1, 30));
            timelabel1.setFont(new Font("", 1, 30));
            timelabel2.setFont(new Font("", 1, 30));
            timebutton1.setFont(new Font("", 1, 18));
            timebutton2.setFont(new Font("", 1, 18));
            timebutton3.setFont(new Font("", 1, 12));
            timebutton4.setFont(new Font("", 1, 12));
            timebutton5.setFont(new Font("", 1, 12));
            timebutton6.setFont(new Font("", 1, 12));
            timebutton7.setFont(new Font("", 1, 12));
            timebutton8.setFont(new Font("", 1, 12));
            add(timelabel1);
            add(timebutton1);
            add(timelabel2);
            add(timebutton2);
            add(timebutton3);
            add(timebutton4);
            add(timebutton5);
            add(timebutton6);
            add(timebutton7);
            add(timebutton8);
            add(label);
            add(close);
            //点击button后，程序终止。
            close.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isStop = true;
                    delay=1;
                    dispose();
                }
            });
            //减小延时按钮
            timebutton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(delay==999){
                        delay=950;
                    }
                    else if(delay>=550){
                        delay-=50;
                    }
                    else if(delay>=220){
                        delay-=20;
                    }
                    else if(delay>=110){
                        delay-=10;
                    }
                    else if(delay>=55){
                        delay-=5;
                    }
                    else if(delay>=1) {
                        delay--;
                    }
                    String delaystr=String.valueOf(delay);
                    if(delaystr.length()==1){
                        delaystr=" "+delaystr;
                    }
                    timelabel2.setText(delaystr);
                }
            });
            //增加延时按钮
            timebutton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(delay>=950) {
                        delay=999;
                    }
                    else if(delay>=550){
                        delay+=50;
                    }
                    else if(delay>=200){
                        delay+=20;
                    }
                    else if(delay>=100){
                        delay+=10;
                    }
                    else if(delay>=50){
                        delay+=5;
                    }
                    else{
                        delay++;
                    }
                    String delaystr=String.valueOf(delay);
                    if(delaystr.length()==1){
                        delaystr=" "+delaystr;
                    }
                    timelabel2.setText(delaystr);
                }
            });
            timebutton3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delay=30;
                    timelabel2.setText(String.valueOf(delay));
                }
            });
            timebutton4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delay=60;
                    timelabel2.setText(String.valueOf(delay));
                }
            });
            timebutton5.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delay=120;
                    timelabel2.setText(String.valueOf(delay));
                }
            });
            timebutton6.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delay=250;
                    timelabel2.setText(String.valueOf(delay));
                }
            });
            timebutton7.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delay=450;
                    timelabel2.setText(String.valueOf(delay));
                }
            });
            timebutton8.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delay=950;
                    timelabel2.setText(String.valueOf(delay));
                }
            });
        }

    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        UIManager.setLookAndFeel(lookAndFeel);

        MouseMove m = new MouseMove();
        m.run();
    }



}
