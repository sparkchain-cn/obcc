package cn.obcc.driver.contract.solc.test;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class AbiFormatParserTest {

 //region example
    /* example
    [
{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},
{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},
{"constant":true,"inputs":[],"name":"decimals","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},
{"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"},{"name":"memos","type":"string"}],"name":"transfer","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},
{"constant":true,"inputs":[{"name":"_owner","type":"address"}],"name":"balanceOf","outputs":[{"name":"balance","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},
{"constant":true,"inputs":[],"name":"symbol","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},
{"constant":true,"inputs":[],"name":"ONE_TOKEN","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},
{"inputs":[{"name":"tokenCode","type":"string"},{"name":"tokenName","type":"string"},{"name":"amount","type":"uint256"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},
{"anonymous":false,"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":true,"name":"_to","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Transfer","type":"event"}
]
     */
   //endregion


    @Test
    public void testAbiFuncInSize() {
    }

    @Test
    public void testGetFunctionInputs() {
    }

    @Test
    public void testGetFunctionOutputs() {
    }

    @Test
    public void testGetFunctionElements() {
    }

    @Test
    public void testGetFunction() {
    }

    @Test
    public void testExist() {
    }

    @Test
    public void testGetConstructor() {
    }

    @Test
    public void testGetMethodNames() {
    }

    @Test
    public void testGetConstructorInputs() {
    }

    @Test
    public void testGetConstructorInputTypes() {
    }

    @Test
    public void testGetFunctionInputTypes() {
    }

    @Test
    public void testGetFunctionInputNames() {
    }

    @Test
    public void testGetFunctionOutputTypes() {
    }
}