package util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class IpAddressUtilTest {

    private IpAddressUtil ipAddressUtil;

    @Before
    public void setUp() {
        ipAddressUtil = new IpAddressUtil();
    }

    @Test
    public void generateIPForServers_returns_valid_ip_addresses_for_valid_input_values() throws IpAddressUtil.InvalidInputException {
        // GIVEN
        String[] ipAddresses = new String[]{"192.168.0.0", "192.168.0.1"};

        // WHEN
        String[] ipForServers = ipAddressUtil.generateIPForServers(1, 2);

        // THEN
        Assert.assertThat(ipForServers, equalTo(ipAddresses));
    }

    @Test(expected = IpAddressUtil.InvalidInputException.class)
    public void generateIPForServers_throws_exception_for_negative_max_pods() throws IpAddressUtil.InvalidInputException {
        // GIVEN
        // WHEN
        ipAddressUtil.generateIPForServers(-1, 2);
    }

    @Test(expected = IpAddressUtil.InvalidInputException.class)
    public void generateIPForServers_throws_exception_for_max_pods_above_twofiftySix() throws IpAddressUtil.InvalidInputException {
        // GIVEN
        // WHEN
        ipAddressUtil.generateIPForServers(257, 2);
    }

    @Test(expected = IpAddressUtil.InvalidInputException.class)
    public void generateIPForServers_throws_exception_for_max_servers_above_twofiftySix() throws IpAddressUtil.InvalidInputException {
        // GIVEN
        // WHEN
        ipAddressUtil.generateIPForServers(0, 257);
    }


    @Test()
    public void isIPValid_should_return_true_for_valid_ip() {
        // GIVEN
        String ipAddress = "192.168.0.255";

        // WHEN
        boolean ipValid = ipAddressUtil.isIPValid(ipAddress);

        // THEN
        Assert.assertEquals(true, ipValid);
    }

    @Test()
    public void isIPValid_should_return_false_for_invalid_ip() {
        // GIVEN
        String ipAddress = "192.168.0.256";

        // WHEN
        boolean ipValid = ipAddressUtil.isIPValid(ipAddress);

        //THEN
        Assert.assertEquals(false, ipValid);
    }

    @Test()
    public void isIPValid_should_return_false_for_short_ip() {
        // GIVEN
        String ipAddress = "192.168.10";

        // WHEN
        boolean ipValid = ipAddressUtil.isIPValid(ipAddress);

        // THEN
        Assert.assertEquals(false, ipValid);
    }

}