package coming.web3.token.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import coming.web3.token.tools.TokenDefinition;

/**
 * Created by James on 10/11/2018.
 * Stormbird in Singapore
 */

public class FunctionDefinition
{
    public ContractInfo contract;
    public String method;
    public TokenDefinition.Syntax syntax;
    public As as;
    public List<MethodArg> parameters = new ArrayList<>();

    public String result;
    public long resultTime = 0;
    public BigInteger tokenId;
    public EthereumTransaction tx;

    public int getTokenRequirement()
    {
        int count = 0;
        for (MethodArg arg : parameters)
        {
            if (arg.isTokenId()) count++;
        }

        if (count == 0) count = 1;

        return count;
    }
}