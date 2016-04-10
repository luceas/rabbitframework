package com.rabbitframework.commons.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import com.rabbitframework.commons.org.springframework.io.Resource;
import com.rabbitframework.commons.org.springframework.io.support.PathMatchingResourcePatternResolver;
import com.rabbitframework.commons.org.springframework.io.support.ResourcePatternResolver;

/**
 * 资源加载类，调用spring中的Resource接口实现
 * 
 * @author Justin
 */
public abstract class MockResource {
	protected static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	/**
	 * 根据参数获取资源对象
	 * 
	 * @param location
	 *            指定文件路径及文件名
	 * @return
	 */
	public static Resource getResource(String location) {
		if (location == null) {
			throw new NullPointerException("param location is not null");
		}
		return getResourcePatternResolver().getResource(location);
	}

	private static ResourcePatternResolver getResourcePatternResolver() {
		return resourcePatternResolver;
	}

	/**
	 * 根据参数路径获取资源并转换为输入流
	 * 
	 * @param location
	 *            指定文件路径及文件名
	 * @return
	 * @throws IOException
	 */
	public static InputStream getResourceAsStream(String location)
			throws IOException {
		Resource resource = getResource(location);
		return resource.getInputStream();
	}

	/**
	 * 根据参数路径获取资源并转换为URI对象
	 * 
	 * @param location
	 *            指定文件路径及文件名
	 * @return
	 * @throws IOException
	 */
	public static URI getResourceAsURI(String location) throws IOException {
		Resource resource = getResource(location);
		return resource.getURI();
	}

	/**
	 * 根据参数路径获取资源并转换为URL对象
	 * 
	 * @param location
	 *            指定文件路径及文件名
	 * @return
	 * @throws IOException
	 */
	public static URL getResourceAsURL(String location) throws IOException {
		Resource resource = getResource(location);
		return resource.getURL();
	}

	/**
	 * 根据参数路径获取资源并转换为File对象
	 * 
	 * @param location
	 *            指定文件路径及文件名
	 * @return
	 * @throws IOException
	 */
	public static File getResourceAsFile(String location) throws IOException {
		Resource resource = getResource(location);
		return resource.getFile();
	}

	/**
	 * 根据参数获取资源对象 参数location可进行文件或路径的匹配
	 * 
	 * @param location
	 * @return
	 */
	public static Resource[] getResources(String location) throws IOException {
		if (location == null) {
			throw new NullPointerException("param location is not null");
		}
		return getResourcePatternResolver().getResources(location);
	}

	/**
	 * 根据参数获取URL集合
	 * 
	 * @param location
	 *            可匹配的路径文件
	 * @return
	 * @throws IOException
	 */
	public static List<URL> getResourceAsURLs(String location)
			throws IOException {
		List<URL> urls = new ArrayList<URL>();
		if (location == null) {
			return urls;
		}
		String[] locations = StringUtils.tokenizeToStringArray(location);
		for (int i = 0; i < locations.length; i++) {
			String path = locations[i];
			Resource[] resources = getResources(path);
			for (Resource resource : resources) {
				urls.add(resource.getURL());
			}
		}
		return urls;
	}

	/**
	 * 根据参数获取URI集合
	 * 
	 * @param location
	 *            可匹配的路径文件
	 * @return
	 * @throws IOException
	 */
	public static List<URI> getResourceAsURIs(String location)
			throws IOException {
		List<URI> uris = new ArrayList<URI>();
		if (location == null) {
			return uris;
		}
		String[] locations = StringUtils.tokenizeToStringArray(location);
		for (int i = 0; i < locations.length; i++) {
			String path = locations[i];
			Resource[] resources = getResources(path);
			for (Resource resource : resources) {
				uris.add(resource.getURI());
			}
		}
		return uris;
	}

	/**
	 * 根据参数获取File集合
	 * 
	 * @param location
	 *            可匹配的路径文件
	 * @return
	 * @throws IOException
	 */
	public static List<File> getResourceAsFiles(String location)
			throws IOException {
		List<File> files = new ArrayList<File>();
		if (location == null) {
			return files;
		}
		String[] locations = StringUtils.tokenizeToStringArray(location);
		for (int i = 0; i < locations.length; i++) {
			String path = locations[i];
			Resource[] resources = getResources(path);
			for (Resource resource : resources) {
				files.add(resource.getFile());
			}
		}
		return files;
	}

	/**
	 * 根据参数获取File集合
	 * 
	 * @param location
	 *            可匹配的路径文件
	 * @return
	 * @throws IOException
	 */
	public static List<InputStream> getResourceAsInputStreams(String location)
			throws IOException {
		List<InputStream> ins = new ArrayList<InputStream>();
		if (location == null) {
			return ins;
		}
		String[] locations = StringUtils.tokenizeToStringArray(location);
		for (int i = 0; i < locations.length; i++) {
			String path = locations[i];
			Resource[] resources = getResources(path);
			for (Resource resource : resources) {
				ins.add(resource.getInputStream());
			}
		}
		return ins;
	}

	/**
	 * 根据参数获取类名(包括包名)
	 * 
	 * @param location
	 *            可匹配的路径文件
	 * @return
	 * @throws IOException
	 */
	public static List<String> getClassNames(String location)
			throws IOException {
		List<String> classNames = new ArrayList<String>();
		Resource[] resources = getResources(location);
		for (int i = 0; i < resources.length; i++) {
			Resource resource = resources[i];
			if (resource.isReadable()) {
				ClassReader classReader = new ClassReader(
						resource.getInputStream());
				ClassNode classNode = new ClassNode();
				classReader.accept(classNode, ClassReader.SKIP_DEBUG);
				String className = classNode.name;
				className = ClassUtils
						.convertResourcePathToClassName(className);
				if (!className.endsWith("package-info")) {
					if (!classNames.contains(className)) { // 在classpath*模式下，去掉重复className
						classNames.add(className);
					}
				}
			}
		}
		return classNames;
	}
}
