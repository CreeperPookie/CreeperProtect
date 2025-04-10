package creeperpookie.creeperprotect.handlers;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.net.SocketAddress;

@ChannelHandler.Sharable
public class PacketHandler extends ChannelDuplexHandler
{
	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
	{
		ctx.bind(localAddress, promise);
	}

	@SubscribeEvent
	public void connect(FMLNetworkEvent.ServerConnectionFromClientEvent event)
	{
		ChannelPipeline pipeline = event.getManager().channel().pipeline();
		pipeline.addBefore("packet_handler", this.getClass().getName(), this);
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
	{
		ctx.connect(remoteAddress, localAddress, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
	{
		ctx.disconnect(promise);
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise)
	{
		ctx.close(promise);
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise)
	{
		ctx.deregister(promise);
	}

	@Override
	public void read(ChannelHandlerContext ctx)
	{
		ctx.read();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof CPacketUseEntity)
		{
			CPacketUseEntity packet = (CPacketUseEntity) msg;
			int entityId = ObfuscationReflectionHelper.getPrivateValue(CPacketUseEntity.class, packet, "entityId");
			for (World world : FMLCommonHandler.instance().getMinecraftServerInstance().worlds)
			{
				Entity entity = world.getEntityByID(entityId);
				if (entity instanceof EntityCreeper && packet.getAction() == CPacketUseEntity.Action.ATTACK)
				{
					ctx.fireChannelReadComplete();
					return;
				}
			}
		}
		super.channelRead(ctx, msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
	{
		if (msg instanceof SPacketEntityStatus)
		{
			SPacketEntityStatus packet = (SPacketEntityStatus) msg;
			int entityId = ObfuscationReflectionHelper.getPrivateValue(SPacketEntityStatus.class, packet, "entityId");
			byte opCode = ObfuscationReflectionHelper.getPrivateValue(SPacketEntityStatus.class, packet, "logicOpcode");
			for (World world : FMLCommonHandler.instance().getMinecraftServerInstance().worlds)
			{
				Entity entity = world.getEntityByID(entityId);
				if (entity instanceof EntityCreeper && isDamagingOpCode(opCode)) return;
			}
		}
		else if (msg instanceof SPacketSoundEffect)
		{
			SPacketSoundEffect packet = (SPacketSoundEffect) msg;
			SoundEvent sound = ObfuscationReflectionHelper.getPrivateValue(SPacketSoundEffect.class, packet, "sound");
			if (sound == SoundEvents.ENTITY_CREEPER_HURT || sound == SoundEvents.ENTITY_CREEPER_DEATH) return;
		}
		ctx.write(msg, promise);
	}

	@Override
	public void flush(ChannelHandlerContext ctx)
	{
		ctx.flush();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx)
	{

	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx)
	{

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{

	}

	private boolean isDamagingOpCode(byte opCode)
	{
		// hurt, death, thorns, drowning and fire
		return opCode == (byte) 2 || opCode == (byte) 3 || opCode == (byte) 33 || opCode == (byte) 36 || opCode == (byte) 37;
	}
}
