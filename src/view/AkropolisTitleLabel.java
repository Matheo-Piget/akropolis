package view;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Iterator;

public class AkropolisTitleLabel extends JPanel {
    private BufferedImage titleImage;
    private Color color;
    private AlphaComposite[] alphas;
    private Queue<Particle> particles = new LinkedList<>();
    private Random random = new Random();
    protected int width = 1024;
    protected int height = 329;
    private int particleCount = 0;
    private int particleLimit = 200;
    private long lastTime = System.currentTimeMillis();

    public AkropolisTitleLabel() {
        setPreferredSize(new Dimension(1024, 329));
        setOpaque(false);

        // Load the title image
        try {
            titleImage = ImageIO.read(getClass().getResource("/akropolisTitle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Orange red color
        color = new Color(255, 69, 0);

        // Create alpha composites for the particles
        alphas = new AlphaComposite[201];
        for (int i = 0; i <= 200; i++) {
            alphas[i] = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, i / 200f);
        }

        // Create a timer to animate the particles
        Timer timer = new Timer(15, e -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > 1000) {
                particleCount = 0;
                lastTime = currentTime;
            }
            if (particleCount < particleLimit) {
                Particle particle = new Particle();
                particles.add(particle);
                particleCount++;
            }
            Iterator<Particle> iterator = particles.iterator();
            while (iterator.hasNext()) {
                Particle particle = iterator.next();
                particle.update();
                if (particle.isDead()) {
                    iterator.remove();
                }
            }
            repaint(); // Repaint the particles
        });
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        for (Particle particle : particles) {
            particle.draw(g2d);
        }
        if (titleImage != null) {
            g2d.drawImage(titleImage, 0, 0, this);
        }
        g2d.dispose();
    }

    // Particle class representing individual particles
    private class Particle {
        private int x, y;
        private int size = 8;
        private int life;

        public Particle() {
            x = width / 2 + random.nextInt(width / 2) - width / 4 - 25;
            y = random.nextInt(((int) (height / 3)), height - 100);
            life = 100;
        }

        public void update() {
            y -= 1; // Move the particle up
            life--; // Decrease the life of the particle
        }

        public void draw(Graphics2D g) {
            g.setComposite(alphas[life]);
            g.setColor(color);
            g.fillOval(x, y, size, size);
            g.setComposite(AlphaComposite.SrcOver);
        }

        public boolean isDead() {
            return life <= 0;
        }
    }
}