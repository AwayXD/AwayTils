package xyz.awayxd.awaytils.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.awayxd.awaytils.commands.ModManager;

import java.util.Random;

public class KillSults implements ModManager.ModLifecycle {
    private Minecraft mc = Minecraft.getMinecraft();
    private Random rand = new Random();

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        IChatComponent message = event.message;
        if (message != null) {
            String cleanMessage = message.getUnformattedText().replaceAll("§.", "");

            String playerName = mc.thePlayer.getName().toLowerCase();
            if (shouldProcessKillMessage(cleanMessage, playerName)) {
                processKillMessage(cleanMessage, playerName);
            }
        }
    }

    private boolean shouldProcessKillMessage(String message, String playerName) {
        String[] killPrefixes = new String[]{"MULTI ", "PENTA ", "QUADRA ", "TRIPLE ", "DOUBLE ", ""};
        for (String prefix : killPrefixes) {
            if (message.startsWith(prefix + "KILL! ")) {
                return true;
            }
        }
        return message.toLowerCase().contains("was killed by " + playerName)
                || message.toLowerCase().contains("was thrown into the void by " + playerName)
                || message.toLowerCase().contains("was thrown off a cliff by " + playerName)
                || message.toLowerCase().contains("was struck down by " + playerName)
                || message.toLowerCase().contains("be sent to davy jones' locker by " + playerName);
    }

    private void processKillMessage(String message, String playerName) {
        String[] insults = {
                "Download Sigma to kick a!s!s while listening to some badass music!",
                "Quick Quiz: I am zeus's son, who am I? Sigma",
                "Bigmama and Sigmama",
                "Sigma never dies",
                "Look a divinity! He definitely uses Sigma!",
                "I am not racist, but I only like Sigma users.",
                "Don't piss me off or you will discover the true power of Sigma's inf reach",
                "Learn your alphabet with the Sigma client: Omikron, Sigma, Epsilon, Alpha!",
                "What should I choose? Sigma or Sigma?",
                "In need of a cute present for Christmas? Sigma is all you need!",
                "Sigmaclient . Info is your new home",
                "I don't hack I just Sigma",
                "I have a good Sigma config, don't blame me",
                "Want some skills? Check out Sigmaclient . Info!",
                "Maybe I will be Sigma, I am already Sigma",
                "Why Sigma? Cause it is the addition of pure skill and incredible intellectual abilities",
                "Wow! My combo is Sigma'n!",
                "You have been oofed by Sigma oof oof",
                "I am a sig-magician, thats how I am able to do all those block game tricks",
                "Sigma utility client no hax 100%",
                "Sigma. The only client run by speakers of Breton",
                "Sigma will help you! Oops, i killed you instead.",
                "Did I really just forget that melody? Si sig sig sig Sigma",
                "Stop Hackusation me cuz im just Sigma",
                "NoHaxJustSigma",
                "Sigma users belike: Hit or miss I guess I never miss!",
                "Order free baguettes with Sigma client",
                "Another Sigma user? Awww man",
                "I dont hack i just have Sigma Gaming Chair",
                "Stop it, get some help! Get Sigma",
                "Do like Tenebrous, subscribe to LeakedPvP!",
                "Hypixel wants to know Sigma owner’s location [Accept] [Deny]",
                "Why are you just here? Just to suffer? Sigma is your only solution",
                "A mother becomes a true grandmother the day she gets Sigma 5.0",
                "Beauty is not in the face; beauty is in Jello for Sigma",
                "No hax just beta testing the anti-cheat with Sigma.",
                "Don't forget to report me for Sigma on the forums!",
                "Sigma helps reducing arm fatigue. Available for free at your local pharmacy.",
                "Don't use Sigma? OK BOOMER",
                "My whole life changed since I discovered Sigma",
                "S. I. G. M. A. Hack with me today!",
                "Mama once told me, use Sigma it's free",
                "Sigma made this world a better place, killing you with it even more",
                "Behind every Sigma user, is an incredibly cool human being. Trust me, cooler than you.",
                "Sigma gang, Sigma gang, Sigma gang, you spent ten racks on this server, i killed you get better.",
                "How come a noob like you not use Sigma?",
                "To cure your Ligma get Sigma",
                "Imagine using anything but Sigma",
                "Fly faster than light, only available in Sigma™",
                "What? You've never downloaded Jello for Sigma? You know it's the best right?",
                "Search sigmaclient , info to get the best mineman skills!",
                "Hello Sigma my old friend...",
                "Sigma is better than Optifine",
                "Your client sucks, just get Sigma",
                "#SwitchToSigma5",
                "Boost your win streak with Sigma!",
                "Subscribe to Mentalfrostbyte on youtube and discover Jello for Sigma!"
        };

        String[] parts = message.split(" ");
        if (parts.length > 3) {
            String victimName = parts[3];
            String insult = String.format(insults[rand.nextInt(insults.length)], victimName);
            mc.thePlayer.sendChatMessage(insult);
        }
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}

