package util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import query.QueryDataModel;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class QueryRecordUtilTest {

    @Mock
    TimeStampUtil timeStampUtil;

    private QueryRecordUtil queryRecordUtil;

    @Before
    public void setUp() {
        queryRecordUtil = new QueryRecordUtil(timeStampUtil);
    }

    @Test
    public void getTokensForRecord_should_generate_comma_separated_tokens_for_record() {
        // GIVEN
        String query = "1414738800,192.168.0.0,0,61";
        String[] tokens = {"1414738800", "192.168.0.0", "0", "61"};

        // WHEN
        String[] tokensForRecord = queryRecordUtil.getTokensFromRecord(query);

        // THEN
        Assert.assertThat(tokens, equalTo(tokensForRecord));
    }

    @Test
    public void createQueryDataModelForRecord_should_return_query_data_model_for_valid_record() {
        // GIVEN
        String query = "1414738800,192.168.0.0,0,61";
        given(timeStampUtil.timeStampToString(1414738800L)).willReturn("foo");
        QueryDataModel model = new QueryDataModel("foo", 1414738800L,0, 61);

        // WHEN
        QueryDataModel queryDataModel = queryRecordUtil.createQueryDataModelFromTokens(query);

        // THEN
        verify(timeStampUtil).timeStampToString(1414738800L);
        Assert.assertEquals(queryDataModel, model);
    }

    @Test
    public void createQueryDataModelForRecord_should_return_null_model_for_empty_record() {
        // GIVEN
        String query = "";

        // WHEN
        QueryDataModel queryDataModel = queryRecordUtil.createQueryDataModelFromTokens(query);

        // THEN
        Assert.assertEquals(null, queryDataModel);
    }

}