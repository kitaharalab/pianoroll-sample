package jp.kthrlab.pianorollsample;

import jp.crestmuse.cmx.processing.CMXController;
import jp.kthrlab.pianoroll.Channel;
import jp.kthrlab.pianoroll.cmx.PianoRollDataModelMultiChannel;
import jp.kthrlab.pianoroll.processing_cmx.HorizontalPAppletCmxPianoRoll;
import processing.core.PApplet;
import processing.event.KeyEvent;

import javax.swing.*;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyApp extends HorizontalPAppletCmxPianoRoll {

    CMXController cmx = CMXController.getInstance();

    IMusicData musicData;

    @Override
    public void draw() {
        super.draw();

    }

    @Override
    public void setup() {
        super.setup();
//        hideDefaultTitleBar();
//        createMenu();

        musicData = new MusicData(
//                "kirakira2.mid",
                "TchaikovskyPletnevMarch.mid",
                timeline.getSpan().intValue(),
                0,
                4,
                48,
                1,
                12
        );

        cmx.smfread(musicData.getScc());

        List<Color> colors = new ArrayList<>(Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN));
        List<Channel> channels = new ArrayList<Channel>();
        try {
            Arrays.stream(musicData.getScc().toDataSet().getPartList()).forEachOrdered(part -> {
                Channel channel = new Channel(
                        part.channel(),
                        part.prognum(),
                        "",
                        colors.get(part.channel())
                );
                channels.add(channel);
            });
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }


        dataModel = new PianoRollDataModelMultiChannel(
                2,
                2 +  12,
                4,
                channels,
                musicData.getScc()
        );

    }

    private void hideDefaultTitleBar() {
        JFrame frame = (JFrame) ((processing.awt.PSurfaceAWT.SmoothCanvas) getSurface().getNative()).getFrame();
        frame.removeNotify();
        frame.setUndecorated(true); // デフォルトのタイトルバーを隠す
        frame.addNotify();
    }

    void startMusic() {
        if (!cmx.isNowPlaying()) {
            cmx.playMusic();
        }
    }

    void stopMusic() {
        cmx.stopMusic();
    }

    void createMenu() {
        JFrame frame = (JFrame) ((processing.awt.PSurfaceAWT.SmoothCanvas) getSurface().getNative()).getFrame();

        JMenuBar menuBar = new JMenuBar();

        Button btnStart = new Button("Start");
        btnStart.addActionListener(e -> { startMusic(); });
        menuBar.add(btnStart);

        Button btnStop = new Button("Stop");
        btnStop.addActionListener(e -> { stopMusic(); });
        menuBar.add(btnStop);

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { MyApp.class.getName() });
    }

    @Override
    public void keyPressed() {
        super.keyPressed();
        switch (keyCode) {
            case ENTER -> startMusic();
            case BACKSPACE -> stopMusic();
        }
    }
}
