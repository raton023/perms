package com.darkania.perms;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener,CommandExecutor{
@Override
public void onEnable() {getServer().getPluginManager().registerEvents(this, this);}

@EventHandler
public void entrando(PlayerJoinEvent e){
//El permiso se quita siempre que sale el jugador por eso lo agregamos aca
List<String> allperm = getConfig().getStringList(e.getPlayer().getName()+".permiso");
for(int i=0;allperm.size()>i;i++){
if(getConfig().getLong(e.getPlayer().getName()+".expira."+allperm.get(i))==0){
e.getPlayer().addAttachment(this,allperm.get(i),true);//este no expira nunca
continue;//se pasa al siguiente i
}
if(getConfig().getLong(e.getPlayer().getName()+".expira."+allperm.get(i))>(System.currentTimeMillis()/1000)){
e.getPlayer().addAttachment(this,allperm.get(i),true,(int) (getConfig().getLong(e.getPlayer().getName()+".expira."+allperm.get(i))-(System.currentTimeMillis()/1000L))*20); 
//este aun no expira		
}
else{
List<String> delperm = getConfig().getStringList(e.getPlayer().getName()+".permiso");
delperm.remove(allperm.get(i));
getConfig().set(e.getPlayer().getName()+".permiso", delperm);
getConfig().set(e.getPlayer().getName()+".expira."+allperm.get(i),null);
saveConfig();
//este ya expiro y se elimina
}}}
	
@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
if(sender instanceof Player){
Player p = (Player)sender;
if(command.getName().equalsIgnoreCase("permset")){
if(args.length==3&&!args[2].contains("[a-zA-Z]+")){
if(p.hasPermission(args[1])&&!p.isOp()){
p.sendMessage("ya tienes ese permiso");return true;
}
List<String> allperm = getConfig().getStringList(args[0]+".permiso");
allperm.add(args[1]);
getConfig().set(args[0]+".permiso", allperm);
if(args[2].equals("0")){
	Bukkit.getPlayer(args[0]).addAttachment(this, args[1], true);
	getConfig().set(args[0]+".expira."+args[1], 0);
}else{
	Bukkit.getPlayer(args[0]).addAttachment(this, args[1], true, Integer.valueOf(args[2])*20);
	getConfig().set(args[0]+".expira."+args[1], (System.currentTimeMillis()/1000L)+Long.valueOf(args[2]));
}
saveConfig();
p.sendMessage("Permiso puesto!");
}else {p.sendMessage("/permset jugador permiso.node segundos");}
}
}else{sender.sendMessage("hola consola");}
return true;
}

}
