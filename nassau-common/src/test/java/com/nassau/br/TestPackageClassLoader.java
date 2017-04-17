package com.nassau.br;

import static org.junit.Assert.assertNotEquals;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;

import com.nassau.br.reflection.PackageClassLoader;

public class TestPackageClassLoader {
	@Test
	public void testPackageClassLoader() throws MalformedURLException {
		List<Class<?>> classes = PackageClassLoader.find("com.nassau.br");
		assertNotEquals("Não foi encontrada nenhuma classe no pacote com.nassau.br.", classes.size(), 0);
	}
}
