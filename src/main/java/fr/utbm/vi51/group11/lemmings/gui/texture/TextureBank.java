package fr.utbm.vi51.group11.lemmings.gui.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import fr.utbm.vi51.group11.lemmings.utils.interfaces.ITextureHandler;
import fr.utbm.vi51.group11.lemmings.utils.statics.FileUtils1;

public class TextureBank
{
	/** Instance of the singleton */
	private final static TextureBank				s_TextureBank	= new TextureBank();

	private final Map<String, Texture>				m_textures;

	private final Map<String, Set<ITextureHandler>>	m_handlers;

	/**
	 * Default private constructor called only once
	 * 
	 * @throws IOException
	 */
	private TextureBank()
	{
		m_textures = new HashMap<String, Texture>();
		m_handlers = new HashMap<String, Set<ITextureHandler>>();
	}

	public void loadTextures(
			final Set<String> _textureIDs) throws IOException
	{
		/* Loads textures from texture files */
		for (String key : _textureIDs)
			loadTexture(key,
					FileUtils1.TEXTURE_DIR.resolve(key + FileUtils1.SPRITESHEET_FILE_EXTENSION));

		/* Sends texture to sprite */
		for (String key : m_handlers.keySet())
			for (ITextureHandler hdl : m_handlers.get(key))
				hdl.receiveTexture(m_textures.get(key));

		/* Clears the handlers */
		m_handlers.clear();
	}

	private void loadTexture(
			final String _key,
			final Path _path) throws IOException
	{
		BufferedImage image = ImageIO.read(_path.toFile());
		m_textures.put(_key, new Texture(_key, image));
	}

	/**
	 * @return Instance of the singleton.
	 */
	public static TextureBank getInstance()
	{
		return s_TextureBank;
	}

	public void getTexture(
			final String _id,
			final ITextureHandler _handler)
	{
		if (m_textures.containsKey(_id))
			_handler.receiveTexture(m_textures.get(_id));
		else
		{
			m_handlers.put(_id, new HashSet<ITextureHandler>());
			m_handlers.get(_id).add(_handler);
		}
	}

	public void destroy()
	{
		m_handlers.clear();
		m_textures.clear();
	}
}
