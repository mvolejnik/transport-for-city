package app.tfc.server.status;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

public class UuidGeneratorTest {

	@Test
	public void testGenerateUuidNotEq() throws StatusUpdateException {
		UUID uuid1 = UuidGenerator.generate("a");
		UUID uuid2 = UuidGenerator.generate("b");
		assertNotEquals(uuid1.toString(), uuid2.toString());
	}
	
	@Test
	public void testGenerateUuidEq() throws StatusUpdateException {
		UUID uuid1 = UuidGenerator.generate("abcdafghijklmnopqrstuvwxyz");
		UUID uuid2 = UuidGenerator.generate("abcdafghijklmnopqrstuvwxyz");
		assertEquals(uuid1.toString(), uuid2.toString());
	}
	
	@Test
  public void testGenerateRandomUuidNotEq() throws StatusUpdateException {
    UUID uuid1 = UuidGenerator.generate();
    UUID uuid2 = UuidGenerator.generate();
    assertNotEquals(uuid1.toString(), uuid2.toString());
  }

}
