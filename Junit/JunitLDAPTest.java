

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertNotNull;

@ContextConfiguration(locations = {"classpath:testConfig.xml"}) //Defines LDAP connection
@RunWith(SpringJUnit4ClassRunner.class)
public class JunitTest1{

@Autowired
private IdentityRoleDao identityRoleDao;

long  prev=0;
	
	@Test
	public void FibonacciGenerator() {
        int elements = 10;
            LongStream.iterate(1, n -> {n+=prev; prev=n-prev; return n;}).           
            limit(elements).forEach(System.out::println);
        
    }
    
    @Test
    public void testCrudOperations() throws Exception {
        IdentityRole identityRole = new IdentityRole();
        identityRole.setCstCustGuid("test-cust-guid");
        identityRole.addCstRoles("test");
        identityRole.addCstAuthLinks("test-auth-link");

        identityRoleDao.create(identityRole);

        IdentityRole role = identityRoleDao.getSingle("cstCustGuid", "test-cust-guid");
        assertEquals(role.getCstRoles().get(0), identityRole.getCstRoles().get(0));
        assertEquals(role.getCstAuthLinks().get(0), identityRole.getCstAuthLinks().get(0));
        assertEquals(role.getCstCustGuid(), identityRole.getCstCustGuid());

        identityRoleDao.delete(identityRole);

        role = identityRoleDao.getSingle("cstCustGuid", "test-cust-guid");
        assertNull(role);
    }
    
    @Test
    public void LDAPTest() throws Exception {
      DirContext ctx = null;
      Properties settings = new Properties();

      settings.put(Context.SECURITY_PRINCIPAL, context.getEnvironmentVariableValue("ldap_user"));  //cn=identity,ou=W7,ou=ACC,o=company
      settings.put(Context.SECURITY_CREDENTIALS, context.getEnvironmentVariableValue("ldap_pwd"));
      settings.put(Context.PROVIDER_URL, context.getEnvironmentVariableValue("ldap_url"));  //ldaps://<host>:2636/
      settings.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

      String dn = "ou=esd,o=Company";
      long startTime = System.currentTimeMillis();
      try {
        ctx = new InitialDirContext(settings);
        String searchFilter = "";
      }catch (Exception e) {
          System.out.println("Exception while processing a clean-up - "+ e.getMessage());
      }
      long endTime = System.currentTimeMillis();
      System.out.println("Total processing time ="+(endTime - startTime)+" ms");
    }
    
     public SearchControls getSearch() {
	    search = new SearchControls();
	    search.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    return search;
	  }
    
    	public List<String> getCustomerByAttribute(String dn, String key, String value,
	      DirContext ctx) {
	    List<String> custGuidList = new ArrayList<String>();
	    try {
	      NamingEnumeration<?> customerSearch = ctx.search(dn, key + "=" + value, getSearch());
	      while (customerSearch.hasMoreElements()) {
	        SearchResult custSearchResult = (SearchResult) customerSearch.next();
	        String custDN = custSearchResult.getNameInNamespace();
	        custGuidList.add(getValue(custDN, "cstCustGuid"));
	      }
	    } catch (Exception ex) {
	      System.out.println("Exception while geting customer with Key [" + key + "] value [" + value+ "] " + ex.getMessage());
	    }
	    return custGuidList;
	  }

	  public int deleteCustomer(String dn, String custGuid, DirContext ctx) {
	    int totalRecordCounter = 0;

	    try {
	      System.out.println("Deleting child login branch under CustGuid [" + custGuid + "]");

	      NamingEnumeration<?> loginBranchSearch =
	          ctx.search("cstCustGuid=" + custGuid + ","+dn,"LoginID=*", getSearch());
	      while (loginBranchSearch.hasMoreElements()) {
	        SearchResult loginSearchResult = (SearchResult) loginBranchSearch.next();
	        String chileLoginBranchDN = loginSearchResult.getNameInNamespace();
	        ctx.unbind(chileLoginBranchDN);
	        System.out.println("Deleted [" + chileLoginBranchDN + "]");
	        totalRecordCounter++;
	      }

	      NamingEnumeration<?> alertBranchSearch =
	          ctx.search("cstCustGuid=" + "" + custGuid + ","+dn, "cn=ALERT", getSearch());
	      while (alertBranchSearch.hasMoreElements()) {
	        SearchResult alertSearchResult = (SearchResult) alertBranchSearch.next();
	        String alertBranchDN = alertSearchResult.getNameInNamespace();
	        ctx.unbind(alertBranchDN);
	        System.out.println("Deleted [" + alertBranchDN + "]");
	        totalRecordCounter++;
	      }

	      NamingEnumeration<?> customerSearch = ctx.search(dn, "cstCustGuid=" + custGuid, getSearch());
	      while (customerSearch.hasMoreElements()) {
	        SearchResult custSearchResult = (SearchResult) customerSearch.next();
	        String custDN = custSearchResult.getNameInNamespace();
	        ctx.unbind(custDN);
	        System.out.println("Deleted customer [" + custDN + "]");
	        totalRecordCounter++;
	      }
	    } catch (Exception ex) {
	      System.out.println("Exception while deleting customer [" + custGuid + "] " + ex.getMessage());
	    }
	    return totalRecordCounter;

	  }

	  public String deleteLogin(String dn, String searchFilter, DirContext ctx) {
	    String loginBranchDN = null;
	    SearchResult searchResult = null;
	    try {
	      NamingEnumeration<SearchResult> results = ctx.search(dn, searchFilter, getSearch());
	      while (results.hasMoreElements()) {
	        searchResult = (SearchResult) results.nextElement();
	        loginBranchDN = searchResult.getNameInNamespace();
	        System.out.println("Deleting [" + loginBranchDN + "]");
	        ctx.unbind(loginBranchDN);
	      }
	    } catch (Exception ex) {
	      System.out.println("Exception while deleting [" + loginBranchDN + "] " + ex.getMessage());
	      return null;
	    }
	    return loginBranchDN;
	  }
}
