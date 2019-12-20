

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import IEISPasswordHelper.HashAlg;
import ServiceConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class IEISPasswordHelperTest {

	@InjectMocks
	IEISPasswordHelper eISPasswordHelper;

	@Mock
	private ServiceConfiguration configurationManager;

	@Before
	public void setup() {
		eISPasswordHelper.setConfigurationManager(configurationManager);

	}

	@Ignore @Test
	public void testgetPBKDF2HashWithSalt() {
		int iteration = 1;
		String salt = "11111";
		String message = "myHash";
		Mockito.when(configurationManager.getPropertyValue("PKDF_KeySize"))
				.thenReturn("24");
		eISPasswordHelper.getPBKDF2HashWithSalt(iteration, salt, message);

	}

	@Ignore @Test
	public void testclearPassToUserPassword() {

		HashAlg alg = HashAlg.MD5;
		String clearpass = "pass";
		byte value = 2;
		byte[] salt = { value };
		byte[] response = eISPasswordHelper.clearPassToUserPassword(clearpass,
				alg, salt);
		assertNotNull(response);
	}

	@Ignore @Test
	public void testclearPassToUserPasswordForSHA() {

		HashAlg alg = HashAlg.SHA;
		String clearpass = "pass";
		byte value = 2;
		byte[] salt = { value };
		byte[] response = eISPasswordHelper.clearPassToUserPassword(clearpass,
				alg, salt);
		assertNotNull(response);
	}

	@Ignore @Test
	public void testclearPassToUserPasswordSMD5() {

		HashAlg alg = HashAlg.SMD5;
		String clearpass = "pass";
		byte value = 2;
		byte[] salt = { value };
		byte[] response = eISPasswordHelper.clearPassToUserPassword(clearpass,
				alg, salt);
		assertNotNull(response);
	}

	@Ignore @Test
	public void testclearPassToUserPasswordSSHA() {

		HashAlg alg = HashAlg.SSHA;
		String clearpass = "pass";
		byte value = 2;
		byte[] salt = { value };
		byte[] response = eISPasswordHelper.clearPassToUserPassword(clearpass,
				alg, salt);
		assertNotNull(response);
	}

	@Ignore @Test
	public void testclearPassToUserPasswordSSHA512() {

		HashAlg alg = HashAlg.SSHA512;
		String clearpass = "pass";
		byte value = 2;
		byte[] salt = { value };
		byte[] response = eISPasswordHelper.clearPassToUserPassword(clearpass,
				alg, salt);
		assertNotNull(response);
	}

	@Ignore @Test
	public void testverifyPassword() {

		String clearpass = "pass";
		String example = "{MD5}";
		byte[] userPassword = example.getBytes();

		boolean response = eISPasswordHelper.verifyPassword(clearpass,
				userPassword);
		assertFalse(response);
	}

	@Ignore @Test
	public void testverifyPasswordforSMD5() {

		String clearpass = "pass";
		String example = "{SMD5}";
		byte[] userPassword = example.getBytes();

		boolean response = eISPasswordHelper.verifyPassword(clearpass,
				userPassword);
		assertFalse(response);
	}

	@Ignore @Test
	public void testverifyPasswordSHA() {

		String clearpass = "pass";
		String example = "{SHA}";
		byte[] userPassword = example.getBytes();

		boolean response = eISPasswordHelper.verifyPassword(clearpass,
				userPassword);
		assertFalse(response);
	}

	@Ignore @Test
	public void testverifyPasswordSSHA() {

		String clearpass = "pass";
		String example = "{SSHA}";
		byte[] userPassword = example.getBytes();

		boolean response = eISPasswordHelper.verifyPassword(clearpass,
				userPassword);
		assertFalse(response);
	}

	@Ignore @Test
	public void testverifyPasswordSSHA512() {

		String clearpass = "pass";
		String example = "{SSHA512}";
		byte[] userPassword = example.getBytes();

		boolean response = eISPasswordHelper.verifyPassword(clearpass,
				userPassword);
		assertFalse(response);
	}

	@Ignore @Test
	public void testgeneratePBKDF2Hash() {

		String message = "MSG";

		Mockito.when(configurationManager.getPropertyValue(Mockito.anyString()))
				.thenReturn("3");
		String response = eISPasswordHelper.generatePBKDF2Hash(message);
		assertNotEquals("{PBKDF2}3$obDQ$SF3+", response);
	}
}
