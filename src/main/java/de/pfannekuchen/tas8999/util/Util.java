package de.pfannekuchen.tas8999.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.vdurmont.emoji.EmojiManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class Util {
	
	/**
	 * Sends a message to the channel
	 * @param channel A message channel
	 * @param message A string message
	 */
	public static void sendMessage(MessageChannel channel, String message) {
		channel.sendMessage(message).queue();
	}
	
	/**
	 * Sends a message to the channel
	 * @param channel A message channel
	 * @param message A message object
	 */
	public static void sendMessage(MessageChannel channel, Message message) {
		channel.sendMessage(message).queue();
	}
	
	/**
	 * Adds an X reactions to the message that deletes the message if it's being pressed
	 * @param channel
	 * @param message
	 */
	public static void sendDeletableMessage(MessageChannel channel, String message) {
		channel.sendMessage(message).queue(msg-> msg.addReaction(EmojiManager.getForAlias(":x:").getUnicode()).queue());
	}
	
	/**
	 * Adds an X reactions to the message that deletes the message if it's being pressed
	 * @param channel
	 * @param message
	 */
	public static void sendDeletableMessage(MessageChannel channel, Message message) {
		channel.sendMessage(message).queue(msg-> msg.addReaction(EmojiManager.getForAlias(":x:").getUnicode()).queue());
	}
	
	/**
	 * Sends a message that is being removed after a specified amount of time
	 * @param channel The channel
	 * @param message The message
	 * @param time The time in seconds after it is being deleted
	 */
	public static void sendSelfDestructingMessage(MessageChannel channel, String message, int time) {
		channel.sendMessage(message).queue(msg -> {
			msg.delete().queueAfter(time, TimeUnit.SECONDS);
		});
	}
	
	/**
	 * Sends a message that is being removed after a specified amount of time
	 * @param channel The channel
	 * @param message The message
	 * @param time The time in seconds after it is being deleted
	 */
	public static void sendSelfDestructingMessage(MessageChannel channel, Message message, int time) {
		channel.sendMessage(message).queue(msg -> {
			msg.delete().queueAfter(time, TimeUnit.SECONDS);
		});
	}
	
	public static void deleteMessage(Message msg) {
		msg.delete().queue();
	}
	
	public static void deleteMessage(Message msg, String reason) {
		msg.delete().reason(reason).queue();
	}
	
	public static boolean hasRole(Member member, String... roleNames) {
		List<Role> roles=member.getRoles();
		
		for(String rolename : roleNames) {
			for(Role role : roles) {
				if(role.getName().equalsIgnoreCase(rolename)) {
					return true;
				}
			}
		}
	
		return false;
	}
	
	/**
	 * @param user The user from an event or an author
	 * @return If the user is the bot account this is running on
	 */
	public static boolean isThisUserThisBot(User user) {
		return user.getJDA().getSelfUser().getIdLong() == user.getIdLong();
	}
	
	/**
	 * @param member The member to check
	 * @return If the member has MESSAGE_MANAGE permissions
	 */
	public static boolean hasEditPerms(Member member) {
		return member.hasPermission(Permission.MESSAGE_MANAGE);
	}
	
	/**
	 * @param member The member to check
	 * @return If the member has ADMINISTRATOR permissions
	 */
	public static boolean hasAdminPerms(Member member) {
		return member.hasPermission(Permission.ADMINISTRATOR);
	}
	
	/**
	 * @param member The member to check
	 * @return If the member has MANAGE_WEBHOOKS permissions
	 */
	public static boolean hasIntegrationPerms(Member member) {
		return member.hasPermission(Permission.MANAGE_WEBHOOKS);
	}
	
	/**
	 * Checks if the bot has reacted with an emote
	 * @param msg The message to check
	 * @param emote The emote to check
	 * @return If the bot has reacted with an emote
	 */
	public static boolean hasBotReactedWith(Message msg, String emote) {
		//Iterate through all reactions
		for(MessageReaction reaction : msg.getReactions()) {
			
			ReactionEmote rEmote=reaction.getReactionEmote();
			if(rEmote.isEmoji()){
				if(rEmote.getEmoji().equals(emote)) {
					return reaction.isSelf();
				}
			} else if(rEmote.isEmote()) {
				if(rEmote.getEmote().getName().equals(emote)) {
					return reaction.isSelf();
				}
			}
		}
		return false;
	}

	/**
	 * Sends an error message from an exception
	 * @param channel
	 * @param e
	 */
	public static void sendErrorMessage(MessageChannel channel, Exception e) {
		String message="The error has no message .__.";
		if(e.getMessage()!=null) {
			message=e.getMessage();
		}
		Message msg=new MessageBuilder(new EmbedBuilder().setTitle("Error ._.").addField(e.getClass().getSimpleName(), message, false).setColor(0xB90000)).build();
		sendDeletableMessage(channel, msg);
	}

	public static Message constructEmbedMessage(String title, String fieldtext, String text, int color) {
		return new MessageBuilder(new EmbedBuilder().setTitle(title).addField(fieldtext, text, false).setColor(color)).build();
	}

}