package view.ui;

import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import view.component.TextureFactory;

public class ScoreDetails extends JPanel {
    JLabel buildingLabel = new JLabel("<html><center>0 x 0 <font color='blue'>★</font> = 0</html>");
    JLabel marketLabel = new JLabel("<html><center>0 x 0 <font color='#CCCC00'>★</font> = 0</html>");
    JLabel templeLabel = new JLabel("<html><center>0 x 0 <font color='purple'>★</font> = 0</html>");
    JLabel barrackLabel = new JLabel("<html><center>0 x 0 <font color='red'>★</font> = 0</html>");
    JLabel gardenLabel = new JLabel("<html><center>0 x 0 <font color='green'>★</font> = 0</html>");

    public ScoreDetails() {
        setOpaque(true);
        setLayout(new java.awt.GridLayout(6, 1));
        JButton back = UIFactory.createStyledButton("Cacher", new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                setSize(0, 0);
            }
        });
        buildingLabel.setHorizontalAlignment(JLabel.CENTER);
        marketLabel.setHorizontalAlignment(JLabel.CENTER);
        templeLabel.setHorizontalAlignment(JLabel.CENTER);
        barrackLabel.setHorizontalAlignment(JLabel.CENTER);
        gardenLabel.setHorizontalAlignment(JLabel.CENTER);
        // Add an image at the left of the label
        buildingLabel.setIcon(new ImageIcon(TextureFactory.getTexture("building")));
        marketLabel.setIcon(new ImageIcon(TextureFactory.getTexture("market")));
        templeLabel.setIcon(new ImageIcon(TextureFactory.getTexture("temple")));
        barrackLabel.setIcon(new ImageIcon(TextureFactory.getTexture("barrack")));
        gardenLabel.setIcon(new ImageIcon(TextureFactory.getTexture("garden")));
        add(back);
        add(buildingLabel);
        add(marketLabel);
        add(templeLabel);
        add(barrackLabel);
        add(gardenLabel);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (width == 0 || height == 0) // Avoid division by 0
            return;
        // Calculate the police size to fit the panel
        int fontSize = Math.min(width / 15, height / 5);
        buildingLabel.setFont(new java.awt.Font("Arial", 0, fontSize));
        marketLabel.setFont(new java.awt.Font("Arial", 0, fontSize));
        templeLabel.setFont(new java.awt.Font("Arial", 0, fontSize));
        barrackLabel.setFont(new java.awt.Font("Arial", 0, fontSize));
        gardenLabel.setFont(new java.awt.Font("Arial", 0, fontSize));
        // Rescale the image to fit the panel
        buildingLabel.setIcon(new ImageIcon(((ImageIcon) buildingLabel.getIcon()).getImage().getScaledInstance(fontSize, fontSize, java.awt.Image.SCALE_SMOOTH)));
        marketLabel.setIcon(new ImageIcon(((ImageIcon) marketLabel.getIcon()).getImage().getScaledInstance(fontSize, fontSize, java.awt.Image.SCALE_SMOOTH)));
        templeLabel.setIcon(new ImageIcon(((ImageIcon) templeLabel.getIcon()).getImage().getScaledInstance(fontSize, fontSize, java.awt.Image.SCALE_SMOOTH)));
        barrackLabel.setIcon(new ImageIcon(((ImageIcon) barrackLabel.getIcon()).getImage().getScaledInstance(fontSize, fontSize, java.awt.Image.SCALE_SMOOTH)));
        gardenLabel.setIcon(new ImageIcon(((ImageIcon) gardenLabel.getIcon()).getImage().getScaledInstance(fontSize, fontSize, java.awt.Image.SCALE_SMOOTH)));
    }

    public void updateScoreInfo(int[] scores, int[] mult) {
        buildingLabel.setText("<html><center>" + scores[0] + " x " + mult[0] + " <font color='blue'>★</font> = " + mult[0] * scores[0] + "</html>");
        marketLabel.setText("<html><center>" + scores[1] + " x " + mult[1] + " <font color='#CCCC00'>★</font> = " + mult[1] * scores[1] + "</html>");
        templeLabel.setText("<html><center>" + scores[2] + " x " + mult[2] + " <font color='purple'>★</font> = " + mult[2] * scores[2] + "</html>");
        barrackLabel.setText("<html><center>" + scores[3] + " x " + mult[3] + " <font color='red'>★</font> = " + mult[3] * scores[3] + "</html>");
        gardenLabel.setText("<html><center>" + scores[4] + " x " + mult[4] + " <font color='green'>★</font> = " + mult[4] * scores[4] + "</html>");
    }
}
