package frc.robot;

import java.util.HashMap;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.LightEffects.FlashEffect;
import frc.robot.LightEffects.LightEffect;
import frc.robot.LightEffects.RainbowEffects;
import frc.robot.LightEffects.SetColor;
import frc.robot.LightEffects.SwapEffect;

public class LaserEyes {

    private final Joystick eyesControllerJoystick;
    private final HashMap<Integer, LightEffect> lightEffects;
    private final AddressableLEDBuffer eyesBuffer;
    private final AddressableLED eyesLED;

    private LightEffect currentEffect;

    public LaserEyes(Joystick lightController, int lightPort, int eyesCount) {
        eyesControllerJoystick = lightController;
        eyesLED = new AddressableLED(lightPort);
        eyesBuffer = new AddressableLEDBuffer(eyesCount);
        eyesLED.setLength(eyesCount);
        
        lightEffects = new HashMap<>();
        lightEffects.put(1, new RainbowEffects(eyesBuffer, 0, 2));
        lightEffects.put(2, new SetColor(eyesBuffer, 255, 0, 0));
        lightEffects.put(3, new FlashEffect(eyesBuffer, 255, 0, 0, 0, 255, 0, 0.1));
        lightEffects.put(4, new SwapEffect(eyesBuffer, 0, 0, 255, 255, 0, 0, 0.25));
        currentEffect = lightEffects.get(1);
        eyesLED.start();
    }

    public void tickEyes() {
        for(Integer key : lightEffects.keySet()) {
            if(eyesControllerJoystick.getRawButtonPressed(key)) {
                currentEffect = lightEffects.get(key);
            }
        }

        eyesLED.setData(currentEffect.tick());
    }
    
    public void setColor(int r, int g, int b){
        for(int i = 0; i < eyesBuffer.getLength(); i++) {
            eyesBuffer.setRGB(i, r, g, b);
        }

        eyesLED.setData(eyesBuffer);
    }
}
