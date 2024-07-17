package com.keiber.junitapp.ejemplotest.junit5_app;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.keiber.junitapp.ejemplotest.junit5_app.exceptions.DineroInsuficienteException;
import com.keiber.junitapp.ejemplotest.junit5_app.models.Banco;
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

	@Test
	void testTransferirDineroCuentas() {
		Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
		Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));
		Banco banco = new Banco();
		banco.setNombre("Banco del Estado");
		banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
		assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
		assertEquals("3000", cuenta1.getSaldo().toPlainString());
	}

	@Test
	void testClonAnterior() {
		Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
		Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

		Banco banco = new Banco();
		banco.addCuenta(cuenta1);
		banco.addCuenta(cuenta2);

		banco.setNombre("Banco del Estado");
		banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

		assertAll(() -> {
			assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
		},
				() -> {
					assertEquals("3000", cuenta1.getSaldo().toPlainString());
				},
				() -> {
					assertEquals(2, banco.getCuentas().size());
				}, () -> {
					assertEquals("Banco del Estado", cuenta1.getBanco().getNombre());
				}, () -> {
					assertEquals("Andres", banco.getCuentas().stream()
							.filter(c -> c.getPersona()
									.equals("Andres"))
							.findFirst().get().getPersona());
				}, () -> {
					assertTrue(banco.getCuentas().stream()
							.filter(c -> c.getPersona()
									.equals("Andres"))
							.findFirst().isPresent());
				}, () -> {
					assertTrue(banco.getCuentas().stream()
							.anyMatch(c -> c.getPersona()
									.equals("Jhon Doe")));
				});
	}
}
