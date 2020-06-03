/*
 * Corona-Warn-App / cwa-verification
 *
 * (C) 2020, T-Systems International GmbH
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package app.coronawarn.verification.service;

import app.coronawarn.verification.VerificationApplication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = VerificationApplication.class)
public class JwtServiceTest {

  @Value("${jwt.secret}")
  private String secret;

  @Autowired
  private JwtService jwTService;

  public JwtServiceTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of validateToken method, of class JwtService.
   */
  @Test
  public void testValidateToken() throws UnsupportedEncodingException {
    String jwToken = getJwtTestData(JwtService.Roles.AUTH_C19_HOTLINE, JwtService.Roles.AUTH_C19_HEALTHAUTHORITY);
    Assert.assertTrue(jwTService.validateToken(jwToken));
  }

  private String getJwtTestData(JwtService.Roles... role) throws UnsupportedEncodingException {
    final Map<String, List<String>> realm_accessMap = new HashMap<>();
    final List<String> roleNames = new ArrayList<>();
    for (JwtService.Roles r : role) {
      roleNames.add(r.getRoleName());
    }

    realm_accessMap.put("roles", roleNames);

    return Jwts.builder()
            .setExpiration(Date.from(Instant.now().plusSeconds(3000)))
            .setIssuedAt(Date.from(Instant.now()))
            .setId("baeaa733-521e-4d2e-8abe-95bb440a9f5f")
            .setIssuer("http://localhost:8080/auth/realms/cwa")
            .setAudience("account")
            .setSubject("72b3b494-a0f4-49f5-b235-1e9f93c86e58")
            .claim("auth_time", "1590742669")
            .claim("iss", "http://localhost:8080/auth/realms/cwa")
            .claim("aud", "account")
            .claim("typ", "Bearer")
            .claim("azp", "verification-portal")
            .claim("session_state", "41cc4d83-e394-4d08-b887-28d8c5372d4a")
            .claim("acr", "0")
            .claim("realm_access", realm_accessMap)
            .claim("resource_access", new HashMap())
            .claim("scope", "openid profile email")
            .claim("email_verified", false)
            .claim("preferred_username", "test")
            .signWith(SignatureAlgorithm.HS256, secret.getBytes("UTF-8"))
            .compact();
  }
}
