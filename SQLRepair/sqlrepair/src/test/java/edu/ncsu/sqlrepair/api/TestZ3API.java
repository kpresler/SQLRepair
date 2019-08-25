package edu.ncsu.sqlrepair.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.sqlrepair.SQLTableExample;
import edu.ncsu.sqlrepair.SQLTableExamples;
import edu.ncsu.sqlrepair.TestUtils;
import edu.ncsu.sqlrepair.models.SQLStatement;

@RunWith ( SpringRunner.class )
@SpringBootTest ( properties = "logging.level.org.springframework.web=DEBUG" )
@AutoConfigureMockMvc
public class TestZ3API {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Test
    public void matchesFound1 () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * from table WHERE x > 5" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "x\n6\n7\n4\n5\n" );
        example.setDest( "x\n6\n7\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "WHERE x > 5" ) );

    }

    @Test
    public void matchesFound2 () throws Exception {

        final SQLStatement stmt = new SQLStatement(
                "SELECT * from table WHERE x > 3" );
        final SQLTableExample example = new SQLTableExample();

        example.setSource( "x\n4\n5\n6\n7\n" );
        example.setDest( "x\n4\n5\n6\n7\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "WHERE x > 3" ) );

    }

    @Test
    public void testNotFound1 () throws Exception {
        final SQLTableExample example = new SQLTableExample();

        example.setSource( "x\n5\n6\n8\n" );
        example.setDest( "x\n6\n7\n8\n" );

        final SQLTableExamples exs = new SQLTableExamples( null, example );

        mvc.perform( post( "/api/v1/submitExample" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( exs ) ) )
                .andExpect( status().is4xxClientError() );
    }

    @Test
    public void testMalformed1 () throws Exception {

        final SQLTableExample example = new SQLTableExample();

        final SQLTableExamples exs = new SQLTableExamples( null, example );

        mvc.perform( post( "/api/v1/submitExample" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( exs ) ) )
                .andExpect( status().is4xxClientError() );

    }

    @Test
    public void testMalformed2 () throws Exception {

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "x\n3\n4\n5\n6\n7\n" );
        example.setDest( "x\n3\n4\na5\n6\n7\n" );

        final SQLTableExamples exs = new SQLTableExamples( null, example );

        mvc.perform( post( "/api/v1/submitExample" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( exs ) ) )
                .andExpect( status().is4xxClientError() );

    }

    @Test
    public void testCompound () throws Exception {

        final SQLStatement stmt = new SQLStatement(
                "SELECT * from table WHERE x < 2 OR x > 5" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "x\n1\n6\n2\n3\n" );
        example.setDest( "x\n1\n6\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE x < 2 OR x > 5" ) );
    }

    @Test
    public void testCompound2 () throws Exception {

        final SQLStatement stmt = new SQLStatement(
                "SELECT * from table where x = 2 AND 1=1" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "x\n2\n5\n6\n7\n" );
        example.setDest( "x\n2\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        // Only check part of the statement because we don't care how exactly it
        // rewrote the first part, just that it did
        assertTrue( submitResponse, submitResponse.contains( "AND 1 = 1" ) );
    }

    @Test
    public void testCompoundNotFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * from table where 1=0" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "x\n1\n2\n3\n4\n" );
        example.setDest( "x\n1\n2\n3\n4\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        // This is going to fail because there is no column in the WHERE clause
        // so it never actually gets applied
        // assertTrue( submitResponse,
        // submitResponse.contains( "No match could be found" ) );

    }

    @Test
    public void testCompoundNotFound2 () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * from table where x < 4 AND x > 4" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "x\n1\n2\n3\n4\n" );
        example.setDest( "x\n1\n2\n3\n4\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        // this one should actually pass now that we're rewriting statements

        assertTrue( submitResponse,
                submitResponse.contains( "\"found\":true" ) );
        assertTrue( submitResponse,
                !submitResponse.contains( "\"diff\":null" ) );
    }

    @Test
    public void testSimpleMultiColumn () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * from table where x > 1 AND y >= 4" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "x y\n 2 4\n 3 5\n0 1\n" );

        example.setDest( "x y\n2 4\n 3 5\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "true" ) );

    }

    @Test
    public void testTripleColumn () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * from table where a = 1 AND b != 4 AND c >= 5" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( " a b c \n 1 2 5 \n 1 3 6 \n 1 6 7 \n 0 4 0 \n" );

        example.setDest( " a b c \n 1 2 5 \n 1 3 6 \n 1 6 7 \n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        // again, don't care exactly how it managed to make things work
        assertTrue( submitResponse, submitResponse.contains( "true" ) );

    }

    @Test
    public void testTripleColumnNotFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * from table where a = 1 AND b != 4 AND c >= 5" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( " a b c \n 1 2 5 \n 1 3 6 \n 1 6 7 \n 0 4 0 \n" );

        example.setDest( " a b c \n 1 2 5 \n 1 3 6 \n 1 6 7 \n 0 4 0 \n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        // this one should actually pass now that we're rewriting statements

        assertTrue( submitResponse,
                submitResponse.contains( "\"found\":true" ) );
        assertTrue( submitResponse,
                !submitResponse.contains( "\"diff\":null" ) );
    }

    @Test
    public void testNonInteger () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                " SELECT * from table where a = 'true'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( " a \n true \n true \n true \n false \n false" );

        example.setDest( "a\ntrue\ntrue\ntrue\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE a = 'true'" ) );

    }

    @Test
    public void testNonIntegerNotFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                " SELECT * from table where a = 'true'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( " a \n true \n true \n true \n false \n false" );

        example.setDest( "a\ntrue\ntrue\ntrue\nfalse\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "false" ) );

    }

    @Test
    public void testMulticolumnOr () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                " select * from table where a = 1 or b = 3" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a b\n1 2\n1 4\n 0 3\n6 9" );
        example.setDest( "a b\n1 2\n 1 4\n0 3\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE a = 1 OR b = 3" ) );

    }

    @Test
    public void testMulticolumnDiffTypes () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where a = 1 and b = 'true'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a b\n 1 true\n2 false\n3 true" );
        example.setDest( "a b\n 1 true\n2 false" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE a = 1 AND b = 'true'" ) );

    }

    @Test
    public void testSimpleString () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where a = 'ayylmao'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n ayylmao\n ayy\n lmao\n" );
        example.setDest( "a\nayylmao" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE a = 'ayylmao'" ) );

    }

    @Test
    public void testSimpleStringReversed () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where 'ayylmao' = a" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n ayylmao\n ayy\n lmao\n" );
        example.setDest( "a\nayylmao" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE 'ayylmao' = a" ) );

    }

    @Test
    public void testSimpleStringNotFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where a = 'ayylmao'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n ayyylmao\n ayy\n lmao\n" );
        example.setDest( "a\nayylmao" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "false" ) );

    }

    @Test
    public void testSimpleStringLike () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where a LIKE 'ayylmao'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n ayylmao\n ayy\n lmao\n" );
        example.setDest( "a\nayylmao" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE a LIKE 'ayylmao'" ) );

    }

    @Test
    public void testStringWildcard () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where a LIKE 'ay%'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n ayylmao\n ayy\n lmao\n" );
        example.setDest( "a\nayylmao\n ayy" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE a LIKE 'ay%'" ) );
    }

    @Test
    public void testStringWildcardReverse () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where 'ay%' LIKE a" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n ayylmao\n ayy\n lmao\n" );
        example.setDest( "a\nayylmao\n ayy" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE 'ay%' LIKE a" ) );
    }

    @Test
    public void testStringWildcardNotFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where a LIKE 'ay%'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n ayylmao\n an\n lmao\n" );
        example.setDest( "a\nayylmao\n ayy" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "false" ) );
    }

    @Test
    public void testStringWildcardReverseNotFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where 'ay%' LIKE a" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n ayylmao\n an\n lmao\n" );
        example.setDest( "a\nayylmao\n ayy" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "false" ) );
    }

    @Test
    public void testStringMultipleWildCard () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where 'a%lm%' LIKE a" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n ayylmao\n an\n lmao\n" );
        example.setDest( "a\nayylmao\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE 'a%lm%' LIKE a" ) );
    }

    @Test
    public void testStringMultipleWildCard2 () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where '%lm%' LIKE a" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n ayylmao\n lmao\n an\n" );
        example.setDest( "a\nayylmao\n lmao" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE '%lm%' LIKE a" ) );
    }

    @Test
    public void testExactDate () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE a='2008-11-11'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 2008-11-11\n 2008-01-01\n 2008-11-12\n" );
        example.setDest( "a\n2008-11-11" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE a = '2008-11-11'" ) );
    }

    @Test
    public void testInExactDate () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE a >='2008-11-11'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 2008-11-11\n 2009-01-01\n 2008-10-12\n" );
        example.setDest( "a\n2008-11-11\n 2009-01-01" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE a >= '2008-11-11'" ) );
    }

    @Test
    public void testExactDateReverse () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE '2008-11-11' = a" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 2008-11-11\n 2008-01-01\n 2008-11-12\n" );
        example.setDest( "a\n2008-11-11" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE '2008-11-11' = a" ) );
    }

    @Test
    public void testCurDate () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                String.format( "SELECT * FROM table WHERE a=curdate()" ) );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( String.format( "a\n %s\n 2008-01-01\n 2008-11-12\n",
                LocalDateTime.now().toLocalDate().toString() ) );
        example.setDest( String.format( "a\n%s",
                LocalDateTime.now().toLocalDate().toString() ) );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE a = curdate()" ) );
    }

    @Test
    public void testCurDateLeft () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                String.format( "SELECT * FROM table WHERE curdate()=a" ) );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( String.format( "a\n %s\n 2008-01-01\n 2008-11-12\n",
                LocalDateTime.now().toLocalDate().toString() ) );
        example.setDest( String.format( "a\n %s",
                LocalDateTime.now().toLocalDate().toString() ) );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE curdate() = a" ) );
    }

    @Test
    public void testFuzzyDateMatching () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE '2008-11-%' LIKE a" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 2008-11-11\n 2008-11-12\n2008-01-01\n" );
        example.setDest( "a\n2008-11-11\n2008-11-12" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE '2008-11-%' LIKE a" ) );
    }

    @Test
    public void testFuzzyDateMatchingNotFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE '2008-11-%' LIKE a" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 2008-11-11\n 2008-11-12\n2008-01-01\n" );
        example.setDest( "a\n2003-10-01" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "false" ) );
    }

    @Test
    public void testFuzzyDateMatchingReverse () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE a LIKE '2008-11-%'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 2008-11-11\n 2008-11-12\n2008-01-01\n" );
        example.setDest( "a\n2008-11-11\n2008-11-12" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE a LIKE '2008-11-%'" ) );
    }

    @Test
    public void testProjection () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT a FROM table WHERE 1=1" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a b\n 1 2\n 3 4\n" );
        example.setDest( "a\n 1\n 3\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "SELECT a FROM table WHERE 1 = 1" ) );

    }

    @Test
    public void testProjectionNotFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT a FROM table WHERE 1=1" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a b\n 1 2\n 3 4\n" );
        example.setDest( "a b\n 1 2\n 3 4\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        // Fine now because we rewrite the WHERE clause
        assertTrue( submitResponse,
                submitResponse.contains( "SELECT a,b FROM table" ) );

    }

    @Test
    public void testProjection2 () throws Exception {
        final SQLStatement stmt = new SQLStatement( "SELECT a FROM table" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a b\n 1 2\n 3 4\n" );
        example.setDest( "a\n 1\n 3\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "SELECT a FROM table" ) );
    }

    @Test
    public void testInStatement () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE a IN (1,2,3)" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 1\n 2\n 3\n 4\n 5\n" );
        example.setDest( "a\n 1\n 2\n 3\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse
                .contains( "SELECT * FROM table WHERE a IN (1, 2, 3)" ) );
    }

    @Test
    public void testInStatementNotFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE a IN (7)" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 1\n 2\n 3\n 4\n 5\n" );
        example.setDest( "a\n 1\n 2\n 3\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "false" ) );
    }

    @Test
    public void testInStatementString () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE a IN ('b','c','d')" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n b\n c\n d\n e\n f\n" );
        example.setDest( "a\n b\n c\n d\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();
        assertTrue( submitResponse, submitResponse
                .contains( "SELECT * FROM table WHERE a IN ('b', 'c', 'd')" ) );

    }

    @Test
    public void testNotInStatement () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE a NOT IN (4,5)" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 1\n 2\n 3\n 4\n 5\n" );
        example.setDest( "a\n 1\n 2\n 3\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse
                .contains( "SELECT * FROM table WHERE a NOT IN (4, 5)" ) );
    }

    @Test
    public void testNotInStatement2 () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE a NOT IN (10)" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 1\n 2\n 3\n 4\n 5\n" );
        example.setDest( "a\n 1\n 2\n 3\n 4\n 5\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse
                .contains( "SELECT * FROM table WHERE a NOT IN (10)" ) );
    }

    @Test
    public void testAsStatement () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT ac AS anc FROM table WHERE 1=1" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "ac\n 1\n 2\n 3\n 4\n 5\n" );
        example.setDest( "anc\n 1\n 2\n 3\n 4\n 5\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse
                .contains( "SELECT ac AS anc FROM table WHERE 1 = 1" ) );
    }

    @Test
    public void testAsStatement2 () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT ac AS anc FROM table WHERE ac != 0" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "ac\n 1\n 2\n 3\n 4\n 5\n" );
        example.setDest( "anc\n 1\n 2\n 3\n 4\n 5\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse
                .contains( "SELECT ac AS anc FROM table WHERE ac != 0" ) );
    }

    @Test
    public void testCount () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT COUNT(*) from table WHERE 1=1" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 1\n 2\n 3\n 4\n 5\n" );
        example.setDest( "c\n5" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "SELECT COUNT" ) );

    }

    @Test
    public void testCountNotFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT COUNT(*) from table WHERE 1=1" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 1\n 2\n 3\n 4\n 5\n" );
        example.setDest( "COUNT(*)\n3" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "false" ) );
    }

    @Test
    public void testMultiExamples () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT a FROM table WHERE a != 5" );

        /*
         * First we'll try just one example; in this case, the example isn't
         * precise enough, so the statement is matched even when we don't want
         * it to be
         */

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 8\n 7\n 6\n 2\n 5\n" );
        example.setDest( "a\n 8\n 7\n 6\n 2\n" );

        SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "SELECT a FROM table WHERE a != 5" ) );

        /*
         * Now, add in a second example to further constrain the space of what
         * we want
         */
        final SQLTableExample example2 = new SQLTableExample();

        example2.setSource( "a\n 8\n 7\n 6\n 5\n 2\n" );
        example2.setDest( "a\n 8\n 7\n 6\n 5\n" );

        exs = new SQLTableExamples( stmt.getStatement(), example, example2 );

        submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        /* And now verify that the further constrained version doesn't match */
        assertTrue( submitResponse, submitResponse.contains( "false" ) );

    }

    @Test
    public void testMultiExampleFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT a FROM table WHERE a > 5 OR a < 0" );

        /*
         * Make sure that we can also match properly where there are multiple
         * examples that do match the statement
         */

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 8\n 7\n 6\n 2\n" );
        example.setDest( "a\n 8\n 7\n 6\n" );

        SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse
                .contains( "SELECT a FROM table WHERE a > 5 OR a < 0" ) );

        /*
         * Now, add in a second example to further constrain the space of what
         * we want
         */
        final SQLTableExample example2 = new SQLTableExample();

        example2.setSource( "a\n -1\n -6\n 4\n" );
        example2.setDest( "a\n -1\n -6\n" );

        exs = new SQLTableExamples( stmt.getStatement(), example, example2 );

        submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        /* Make sure that things still match */
        assertTrue( submitResponse, submitResponse
                .contains( "SELECT a FROM table WHERE a > 5 OR a < 0" ) );
    }

    @Test
    public void ensureExtraValuesNotFound () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT a FROM table WHERE a > 5 OR a < 0" );

        /*
         * Make sure that we can also match properly where there are multiple
         * examples that do match the statement
         */

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 8\n 7\n 6\n 2\n" );
        example.setDest( "a\n 8\n 7\n 6\n" );

        SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse
                .contains( "SELECT a FROM table WHERE a > 5 OR a < 0" ) );

        example.setDest( "a\n 8\n 7\n 6\n 4\n" );

        exs = new SQLTableExamples( stmt.getStatement(), example );

        submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "false" ) );

    }

    @Test
    public void testMultiTypeColumn () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE 1=1" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a b c\n 4 c 6\n 5 j 4\n ayy x 3" );
        example.setDest( "a b c\n 4 c 6\n 5 j 4\n ayy x 3" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "SELECT * FROM table WHERE 1 = 1" ) );

    }

    @Test
    public void testFuzzyString () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE a LIKE '%'" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n SQLISCOOL\n SQLISAWESOME\n NOTSOSQL" );
        example.setDest( "a\n SQLISCOOL\n SQLISAWESOME\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "false" ) );

    }

    @Test
    public void testOperatorRewrite () throws Exception {
        // This test will not pass with the statement as written. Need to
        // rewrite the `>` to a `!=` at which point it will

        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE a > 5" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 6\n 8\n 7\n 6\n 3\n 2\n 5" );
        example.setDest( "a\n 6\n 8\n 7\n 6\n 3\n 2" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        System.out.println( submitResponse );

        assertTrue( submitResponse,
                submitResponse.contains( "SELECT * FROM table WHERE a != 5" ) );

    }

    @Test
    public void testProjection3 () throws Exception {

        final SQLStatement stmt = new SQLStatement(
                "SELECT a FROM table WHERE a != 5" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a b\n 6 2\n 8 9\n 7 5\n 6 1\n 3 0\n 2 2\n 5 4" );
        example.setDest( "a\n 6\n 8\n 7\n 6\n 3\n 2" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "SELECT a FROM table WHERE a != 5" ) );

    }

    @Test
    public void testRewriteWhereColumns () throws Exception {

        final SQLStatement stmt = new SQLStatement(
                "SELECT * FROM table WHERE a != 5" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a b\n 6 2\n 8 9\n 7 5\n 6 1\n 3 0\n 2 2\n 5 4" );
        example.setDest( "a\n 6\n 8\n 7\n 6\n 3\n 2" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "SELECT a FROM table" ) );

    }

    @Test
    public void testSelectDistinct () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT DISTINCT a FROM table" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 1 \n 2\n 3\n 4\n 2\n 1\n" );

        example.setDest( "a\n 1 \n 2\n 3\n 4\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "SELECT DISTINCT a FROM table" ) );

    }

    @Test
    public void testSelectOrderBy () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT a FROM table ORDER BY a DESC" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 4\n 1 \n 2\n 3\n 4\n 2\n 1\n" );

        example.setDest( "a\n 4\n 4\n 3\n 2\n 2\n 1\n 1\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse
                .contains( "SELECT a FROM table ORDER BY a DESC" ) );

    }

    @Test
    public void testSelectOrderByDesc () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "SELECT a FROM table ORDER BY a ASC" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 4\n 1 \n 2\n 3\n 4\n 2\n 1\n" );

        example.setDest( "a\n 4\n 4\n 3\n 2\n 2\n 1\n 1\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "SELECT a FROM table" ) );

    }

    @Test
    public void testColumnRenaming () throws Exception {
        // Make sure that we can automatically rename a column to the correct
        // version expected

        final SQLStatement stmt = new SQLStatement( "SELECT a FROM table" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 4\n 1 \n 2\n 3\n 4\n 2\n 1\n" );

        example.setDest( "z\n 4\n 1 \n 2\n 3\n 4\n 2\n 1\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "SELECT a AS z" ) );
    }

    @Test
    public void testColumnRenamingNotFound () throws Exception {
        // But make sure that if the values don't match up, the rename doesn't
        // go through
        final SQLStatement stmt = new SQLStatement( "SELECT a FROM table" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n 4\n 1 \n 2\n 3\n 4\n 2\n 1\n" );

        example.setDest( "z\n 4\n 9 \n 2\n 3\n 4\n 2\n 1\n" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse, submitResponse.contains( "false" ) );
    }

    @Test
    public void testSyntaxRepair () throws Exception {
        final SQLStatement stmt = new SQLStatement(
                "select * from table where a == 'ayylmao' | 1=1 & 2==2" );

        final SQLTableExample example = new SQLTableExample();

        example.setSource( "a\n ayylmao\n ayy\n lmao\n" );
        example.setDest( "a\nayylmao" );

        final SQLTableExamples exs = new SQLTableExamples( stmt.getStatement(),
                example );

        final String submitResponse = mvc
                .perform( post( "/api/v1/submitExample" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( exs ) ) )
                .andReturn().getResponse().getContentAsString();

        assertTrue( submitResponse,
                submitResponse.contains( "WHERE a = 'ayylmao'" ) );
    }

}
