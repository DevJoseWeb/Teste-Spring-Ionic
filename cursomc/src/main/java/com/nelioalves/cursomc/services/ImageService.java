package com.nelioalves.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nelioalves.cursomc.services.exceptions.FileException;

@Service
public class ImageService {

	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		// pega a extensao do arquivo
		String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			if ("png".equals(ext)) {
				img = pngToJpg(img);
			}
			return img;
		} catch (Exception e) {
			throw new FileException("Erro ao ler o arquivo");
		}
	}

	public BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		// alguns png tem fundo transparente, e entao preenche com cor branca
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		return null;
	}

	// encapsula a leitura inputstream
	// o metodo q faz o upload, recebe um inputstream, precisa obter um inputstream
	// atraves de um bufferedimage
	public InputStream getInputStream(BufferedImage img, String extension) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (Exception e) {
			throw new FileException("Erro ao ler o arquivo");
		}
	}
	
	public BufferedImage cropSquare(BufferedImage sourceImg) {
		int min = (sourceImg.getHeight() <= sourceImg.getWidth() ? sourceImg.getHeight() : sourceImg.getWidth());
		
		return Scalr.crop(
				sourceImg, 
				(sourceImg.getWidth()/2) - (min/2), 
				(sourceImg.getHeight()/2) - (min/2), 
				min,
				min);
	}

	public BufferedImage resize(BufferedImage sourceImg, int size) {
		//coloca em qualidade alta p evitar perder qualidade
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY,  size);
	}
}