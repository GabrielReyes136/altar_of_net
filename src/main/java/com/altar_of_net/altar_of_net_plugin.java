package com.altar_of_net;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ChatInput;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.chat.ChatCommandManager;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.plugins.chatcommands.ChatCommandsConfig;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import com.altar_of_net.EightBall;
import com.altar_of_net.AltarOfNetUtils;
import static net.runelite.api.ChatMessageType.CLAN_CHAT;

@Slf4j
@PluginDescriptor(
	name = "The altar of net"
)
public class altar_of_net_plugin extends Plugin
{
	private boolean isPlayerInAltarOfNet;
	//public static final String LOG_CURRENT_CLAN = "!developer-log-current-clan";
	public static final String EVERYBODY_TARGET_X = "!kill";
	public static final String EVENT_ALERT = "!event";
	public static final String SLAP_COMMAND = "!slap";
	public static final String TANK_ENTER_CORPOREAL_BEAST = "!pre";
	public static final String EVERYONE_ENTER_CORPOREAL_BEAST = "!bgs";
	public static final String DICER_COMMAND = "!dice";
	public static final String COMMAND_HELPER = "!help";
	public static final String RANKS_HELPER = "!ranks";
	public static final String GREET_COMMAND_STRING = "!greet";
	public static final String EIGHT_BALL_COMMAND_STRING = "!8ball";
	public static final String LASER_COMMAND = "!laser";
	public static final String MUTE_COMMAND = "!mute";

	public static final String BEG_COMMAND = "!beg";
	public static final String GIVE_COMMAND = "!give";

	public static final String GRATZ_COMMAND = "!gratz";



	private long my_time;
	private EightBall altarOfNetEightBall;
	private EightBall altarOfNetEightBall2;
	@Inject
	private Client client;

	@Inject
	private ChatCommandManager altarOfNetChatCommandManager;

	/*@Inject
	private altar-of-net-config config;
	*/


	private ClanChannel myCurrentClanChannel;

	@Override
	protected void startUp() throws Exception
	{
		myCurrentClanChannel = null;
		isPlayerInAltarOfNet = false;
		altarOfNetEightBall = new EightBall();
		altarOfNetEightBall2 = new EightBall(new String[] {"1", "2","3", "4","5","6"}, 6);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);


		try {
			
			 
			/*
			DEPRECATED : DEVELOPER MODE ONLY!!!!! DO NOT USE
			altarOfNetChatCommandManager.registerCommand(NULL, NULL);
			*/
			
			
			altarOfNetChatCommandManager.registerCommand(GREET_COMMAND_STRING, this::greet_handler);
			altarOfNetChatCommandManager.registerCommand(RANKS_HELPER, this::ranks_handler);
			altarOfNetChatCommandManager.registerCommand(MUTE_COMMAND, this::mute_handler);
			altarOfNetChatCommandManager.registerCommand(SLAP_COMMAND, this::slap_handler);
			altarOfNetChatCommandManager.registerCommand(EVENT_ALERT, this::event_handler);
			altarOfNetChatCommandManager.registerCommand(EVERYBODY_TARGET_X, this::kill_handler);
			altarOfNetChatCommandManager.registerCommand(COMMAND_HELPER, this::commands_handler);
			altarOfNetChatCommandManager.registerCommand(TANK_ENTER_CORPOREAL_BEAST, this::prebgs);
			altarOfNetChatCommandManager.registerCommand(EVERYONE_ENTER_CORPOREAL_BEAST, this::bgs);
			altarOfNetChatCommandManager.registerCommand(BEG_COMMAND, this::beg_handler);
			altarOfNetChatCommandManager.registerCommand(GIVE_COMMAND, this::give_handler);
			
			/* DEPRECATED: DEVELOPER MODE ONLY!!!! DO NOT USE
			altarOfNetChatCommandManager.registerCommand(NULL, NULL);
			altarOfNetChatCommandManager.registerCommand(NULL, NULL);
			*/
			altarOfNetChatCommandManager.registerCommand(GRATZ_COMMAND, this::gratz_handler);

		} catch(NullPointerException e) {

			e.printStackTrace(pw);
			
		}


		
	}

	



	@Override
	protected void shutDown() throws Exception
	{
		//altarOfNetChatCommandManager.unregisterCommand(EIGHT_BALL_COMMAND_STRING);
		altarOfNetChatCommandManager.unregisterCommand(GREET_COMMAND_STRING);
		altarOfNetChatCommandManager.unregisterCommand(RANKS_HELPER);
		altarOfNetChatCommandManager.unregisterCommand(MUTE_COMMAND);
		altarOfNetChatCommandManager.unregisterCommand(SLAP_COMMAND);
		altarOfNetChatCommandManager.unregisterCommand(EVENT_ALERT);
		altarOfNetChatCommandManager.unregisterCommand(EVERYBODY_TARGET_X);
		altarOfNetChatCommandManager.unregisterCommand(COMMAND_HELPER);
		altarOfNetChatCommandManager.unregisterCommand(TANK_ENTER_CORPOREAL_BEAST);
		altarOfNetChatCommandManager.unregisterCommand(EVERYONE_ENTER_CORPOREAL_BEAST);
		altarOfNetChatCommandManager.unregisterCommand(BEG_COMMAND);
		altarOfNetChatCommandManager.unregisterCommand(GIVE_COMMAND);
		//altarOfNetChatCommandManager.unregisterCommand(DICER_COMMAND);
		//altarOfNetChatCommandManager.unregisterCommand(LASER_COMMAND);
		altarOfNetChatCommandManager.unregisterCommand(GRATZ_COMMAND);
		
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		/*
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "ALTAR OF NET MESSAGE: this plug-in will only work if you are part of the clan: altar of net", null);
		}
		*/
	}


	private boolean validate_client( String caller) {

			myCurrentClanChannel = client.getClanChannel();
			if(myCurrentClanChannel != null) {
				if (myCurrentClanChannel.getName().equals("altar of net"))
					isPlayerInAltarOfNet = true;
				else {
					isPlayerInAltarOfNet = false;
					//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "you must be a member of the altar of net clan to use this command" , null);
				}
			}
			
			
			return isPlayerInAltarOfNet;
	}
	private void gratz_handler( ChatMessage chatMessage, String Message) {
		if ( !validate_client("gratz-handler") ) {
			//output error message
			return;
		}
		String[] t0 = Message.split(" ", 2);
		ChatMessageBuilder myBuilder = new ChatMessageBuilder();
		String response;
		if (t0.length < 2) {
		response = myBuilder.append(ChatColorType.NORMAL)
				.append("@@@@ GRATZ @@@@@ from ")
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage.getName())
				.build();
		} else {
			response = myBuilder.append(ChatColorType.HIGHLIGHT)
					.append(t0[1])
					.append(ChatColorType.NORMAL)
					.append(", @@@@ GRATZ @@@@@ from ")
					.append(ChatColorType.HIGHLIGHT)
					.append(chatMessage.getName())
					.build();
		}
		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();
	}
	
	/* DO NOT USE THIS HANDLER! IT WILL BREAK THE PROGRAM
	private void _handler( ChatMessage chatMessage, String Message) {
		if ( !validate_client("NULL") ) {
			//output error message
			return;
		}
		String[] t0 = Message.split(" ", 2);
		if( t0.length != 2) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "you used the command incorrectly." , null);
			return;
		}



	}
	
	private void NULL( ChatMessage chatMessage, String Message) {
		if ( !validate_client("NULL") ) {
			//output error message
			return;
		}

		String response = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage.getName())
				.append(ChatColorType.NORMAL)
				.append(" GET THE FUCK OUT OF HERE ")
				.append(ChatColorType.HIGHLIGHT)
				.append(the_string)
				.build();
		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();

	}
	private void NULLE( ChatMessage chatMessage, String Message) {
		if ( !validate_client("NULL") ) {
			//output error message
			return;
		}
		String[] t0 = Message.split(" ", 2);
		String t1;
		if( t0.length != 2)
			return;
		t1 = t0[1];


		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();

	}
	*/
	private void greet_handler( ChatMessage chatMessage, String Message) {
		if ( !validate_client("greet-handler") ) {
			//output error message
			return;
		}
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
		String[] t0 = Message.split(" ", 2);
		String t1;
		if(t0.length == 1) {
			t1 = chatMessage.getName();
		}
		else {
			if (t0[1].equals("self")) {
				t1 = chatMessage.getName();
			}
			else {
				t1 = t0[1];
			}
		}
		String response = new ChatMessageBuilder()
				.append(ChatColorType.NORMAL)
				.append("The altar welcomes ")
				.append(ChatColorType.HIGHLIGHT)
				.append(t1 )
				.append(ChatColorType.NORMAL)
				.append(" at ")
				.append(Instant.now().atZone(ZoneOffset.UTC).format(format) )
				.build();
		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();
	}
	private void commands_handler( ChatMessage chatMessage, String Message) {
		if ( !validate_client("commands-handler") ) {
			//output error message
			return;
		}

		String response = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage.getName())
				.append(ChatColorType.NORMAL)
				.append(", The chat commands are ")
				.append(ChatColorType.HIGHLIGHT)
				.append("!greet !slap !kill !beg !give !gratz")
				.build();
		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();
	}
	private void prebgs( ChatMessage chatMessage, String Message) {
		if ( !validate_client("prebgs-handler") ) {
			//output error message
			return;
		}

		String response = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append("[PRE BGS PHASE] ")
				.append(ChatColorType.NORMAL)
				.append("It is time for only the tank to enter the corporeal beast lair")
				.build();
		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();
	}
	private void bgs( ChatMessage chatMessage, String Message) {
		if ( !validate_client("bgs-handler") ) {
			//output error message
			return;
		}

		String response = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append("[BGS PHASE] ")
				.append(ChatColorType.NORMAL)
				.append("EVERYONE ENTER THE CORPOREAL BEAST LAIR!!!")
				.build();
		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();
	}
	private void beg_handler( ChatMessage chatMessage, String Message) {
		if ( !validate_client("beg-handler") ) {
			//output error message
			return;
		}
		String[] t0 = Message.split(" ", 2);
		if (t0.length != 2) return;
		String response = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage.getName())
				.append(ChatColorType.NORMAL)
				.append(" is ")
				.append(ChatColorType.HIGHLIGHT)
				.append("BEGGING")
				.append(ChatColorType.NORMAL)
				.append(" for ")
				.append(t0[1])
				.build();
		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();
	}
	private void give_handler( ChatMessage chatMessage, String Message) {
		if ( !validate_client("give-handler") ) {
			//output error message
			return;
		}
		String[] t0 = Message.split(" ", 2);
		if (t0.length != 2) return;
		String response = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage.getName())
				.append(ChatColorType.NORMAL)
				.append(" is ")
				.append(ChatColorType.HIGHLIGHT)
				.append("GIVING ")
				.append(ChatColorType.NORMAL)
				.append(t0[1])
				.build();
		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();
	}

	private void kill_handler( ChatMessage chatMessage, String Message) {
		if ( !validate_client("kill-handler") ) {
			//output error message
			return;
		}
		String[] t0 = Message.split(" ", 2);
		if (t0.length != 2) return;
		String response = new ChatMessageBuilder()
				.append(ChatColorType.NORMAL)
				.append("I want to attack ")
				.append(ChatColorType.HIGHLIGHT)
				.append(t0[1])
				.append(ChatColorType.NORMAL)
				.append(" everybody ")
				.append(ChatColorType.HIGHLIGHT)
				.append(" ATTACK ")
				.append(t0[1])
				.append("!")
				.build();
		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();
	}
	private void mute_handler( ChatMessage chatMessage, String Message) {
		if ( !validate_client("mute-handler") ) {
			//output error message
			return;
		}
	String[] t0 = Message.split(" ", 2);
	if (t0.length <= 1) return;
	if(Message != null) {
		if (Message.isEmpty()) return;
		String response = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(t0[1])
				.append(ChatColorType.NORMAL)
				.append(" has been banished by ")
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage.getName())
				.build();
		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();
	}
}
	private void slap_handler( ChatMessage chatMessage, String Message) {
		if ( !validate_client("slap-handler") ) {
			//output error message
			return;
		}


		String[] t0 = Message.split(" ", 2);
		if (t0.length <= 1) return;
		if(Message != null) {
			if (Message.isEmpty()) return;
			String response = new ChatMessageBuilder()
					.append(ChatColorType.HIGHLIGHT)
					.append(t0[1])
					.append(ChatColorType.NORMAL)
					.append(" has been slapped by ")
					.append(ChatColorType.HIGHLIGHT)
					.append(chatMessage.getName())
					.build();
			final MessageNode myNode = chatMessage.getMessageNode();
			myNode.setRuneLiteFormatMessage(response);
			client.refreshChat();
		}
	}
	private void ranks_handler( ChatMessage chatMessage, String Message) {
		if ( !validate_client("ranks-handler") ) {
			//output error message
			return;
		}

		String response = new ChatMessageBuilder()
				.append(ChatColorType.NORMAL)
				.append("TBOW:")
				.append(ChatColorType.HIGHLIGHT)
				.append("legacy ")
				.append(ChatColorType.NORMAL)
				.append("BLUE:")
				.append(ChatColorType.HIGHLIGHT)
				.append("low lv " )
				.append(ChatColorType.NORMAL)
				.append("RED:")
				.append(ChatColorType.HIGHLIGHT )
				.append("mid lv ")
				.append(ChatColorType.NORMAL)
				.append("PIETY:")
				.append(ChatColorType.HIGHLIGHT)
				.append("pvm ")
				.append(ChatColorType.NORMAL)
				.append("CAPE:" )
				.append(ChatColorType.HIGHLIGHT)
				.append("god ")
				.append(ChatColorType.NORMAL)
				.append("SKULL:")
				.append(ChatColorType.HIGHLIGHT)
				.append("pker ")
				.append(ChatColorType.NORMAL)
				.append("YELLOW:")
				.append(ChatColorType.HIGHLIGHT)
				.append("plebian")
				.build();

		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		client.refreshChat();
	}
	private void event_handler( ChatMessage chatMessage, String Message) {
		if ( !validate_client("event-handler") ) {
			//output error message
			return;
		}
	    //usage !event {EVENT STRING} {WORLD} {SHARE | FFA}
		String[] t0 = Message.split(" ", 4);
		String t1;
		String t2;
		if (t0 != null)
			if (t0.length != 4) return;
		switch (t0[1]) {
			case "dk" : t1 = "Daganoth Kings"; break;
			case "bandos": t1 = "Bandos GWD BOSS"; break;
			case "sara" : t1 = "Sara GWD BOSS"; break;
			case "zammy": t1 = "Zammy GWD BOSS"; break;
			case "arma": t1 = "Arma GWD BOSS"; break;
			case "corp": t1 = "Corporeal Beast"; break;
			case "cox": t1 = "Chambers of Xeric"; break;
			case "pk": t1 = "Clan PK Trip"; break;
			case "wt": t1 = "Wintertodt"; break;
			case "nex": t1 = "Nex GWD BOSS"; break;
			case "tob": t1 = "Theatre of Blood"; break;
			case "gb": t1 = "Group Barrows"; break;
			case "kbd": t1 = "King Black Dragon"; break;
			case "z": t1 = " Group Zulrah"; break;
			case "v": t1= "Group Vorkath"; break;
			case "toa": t1= "RAIDS III"; break;
			case "n": t1 = "the nightmare"; break;
			case "cg": t1 = "group corrupted gauntlet"; break;
			case "chaos": t1 = "chaos elemental"; break;
			default:  t1 = t0[1]; break;
		}
		switch (t0[3]) {
			case ("ffa") : t2 = "Free For All"; break;
			case ("split") : t2 = "Split All Drops"; break;
			default : t2 = "Split All Drops"; break;
		}
		String response = new ChatMessageBuilder()
				.append(ChatColorType.NORMAL)
				.append("CLAN EVENT ALERT: ")
				.append(ChatColorType.HIGHLIGHT)
				.append(t1)
				.append(ChatColorType.NORMAL)
				.append(" WORLD:")
				.append(ChatColorType.HIGHLIGHT)
				.append(t0[2])
				.append(ChatColorType.NORMAL)
				.append(" LOOT SHARE OPTIONS: ")
				.append(ChatColorType.HIGHLIGHT)
				.append(t2)
				.build();

		final MessageNode myNode = chatMessage.getMessageNode();
		myNode.setRuneLiteFormatMessage(response);
		//client.addChatMessage(ChatMessageType.GAMEMESSAGE, null, response, null);
		client.refreshChat();
	}

/*
	@Provides
	altar_of_net_config provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(altar_of_net_config.class);
	}
	*/


}
