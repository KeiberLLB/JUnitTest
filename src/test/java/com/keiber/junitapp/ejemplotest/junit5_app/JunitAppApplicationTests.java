package com.keiber.junitapp.ejemplotest.junit5_app;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.keiber.junitapp.ejemplotest.junit5_app.exceptions.DineroInsuficienteException;
import com.keiber.junitapp.ejemplotest.junit5_app.models.Cuenta;

class JunitAppApplicationTests {

	@Test
	void testNombreCuenta() {
		Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
		// cuenta.setPersona("Andres");
		String esperado = "Andres";
		String real = cuenta.getPersona();
		assertEquals(esperado, real);
		assertTrue(real.equals("Andres"));
	}

	@Test
	void testSaldoCuenta() {
		Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
		assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
		// este test aplica para determinar que un valor NO sea negativo
		assertNotNull(cuenta.getSaldo());
		assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
		assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
	}

	@Test
	void testRefenciaCuenta() {
		Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
		Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
		// se le pasa primero el valor de comparacion y de segundo el valor "real"
		// assertNotEquals(cuenta2, cuenta);
		assertEquals(cuenta2, cuenta);
	}

	@Test
	void testDebitoCuenta() {
		Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
		cuenta.debito(new BigDecimal(100));
		assertNotNull(cuenta.getSaldo());
		assertEquals(900, cuenta.getSaldo().intValue());
		assertEquals("900.12345", cuenta.getSaldo().toPlainString());

	}

	@Test
	void testCreditoCuenta() {
		Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
		cuenta.credito(new BigDecimal(100));
		assertNotNull(cuenta.getSaldo());
		assertEquals(1100, cuenta.getSaldo().intValue());
		assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
	}

	@Test
	void testDineroInsuficienteExceptionCuenta() {
		Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
		// el primer dato que pasamos es la funcion de exceptions que deseamos testear
		// el segundo valor, en este caso un funcion Lambda donde se creará el ambiente
		// de prueba
		Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
			cuenta.debito(new BigDecimal(1500));
		});
		String actual = exception.getMessage();
		String esperado = "Dinero Insuficiente";
		assertEquals(esperado, actual);
	}

}
