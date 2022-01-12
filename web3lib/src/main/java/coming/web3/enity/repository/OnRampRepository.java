package coming.web3.enity.repository;

import android.content.Context;
import android.net.Uri;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

import coming.web3.C;
import coming.web3.enity.repository.tokens.Token;
import coming.web3.util.Utils;

public class OnRampRepository implements OnRampRepositoryType {
    public static final String DEFAULT_PROVIDER = "Ramp";
    private static final String RAMP = "ramp";
    private static final String ONRAMP_CONTRACTS_FILE_NAME = "onramp_contracts.json";

    static
    {
        System.loadLibrary("keys");
    }

    private final Context context;

    public OnRampRepository(Context context)
    {
        this.context = context;
    }

    public static native String getRampKey();

    @Override
    public String getUri(String address, Token token)
    {
        OnRampContract contract = getContract(token);

        AnalyticsProperties analyticsProperties = new AnalyticsProperties();
        analyticsProperties.setData(contract.getSymbol());

        switch (contract.getProvider().toLowerCase())
        {
            case RAMP:
            default:
                return buildRampUri(address, contract.getSymbol()).toString();
        }
    }

    @Override
    public OnRampContract getContract(Token token)
    {
        Map<String, OnRampContract> contractMap = getKnownContracts();
        OnRampContract contract = contractMap.get(token.getAddress().toLowerCase());
        if (contract != null) return contract;
        else
        {
            if (token.isEthereum()) return new OnRampContract(token.tokenInfo.symbol);
            else return new OnRampContract();
        }
    }

    private Map<String, OnRampContract> getKnownContracts()
    {
        return new Gson().fromJson(Utils.loadJSONFromAsset(context, ONRAMP_CONTRACTS_FILE_NAME),
                new TypeToken<Map<String, OnRampContract>>() {
                }.getType());
    }

    private Uri buildRampUri(String address, String symbol)
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("buy.ramp.network")
                .appendQueryParameter("hostApiKey", getRampKey())
                .appendQueryParameter("hostLogoUrl", C.ALPHAWALLET_LOGO_URI)
                .appendQueryParameter("hostAppName", "AlphaWallet")
                .appendQueryParameter("userAddress", address);

        if (!symbol.isEmpty())
        {
            builder.appendQueryParameter("swapAsset", symbol);
        }

        return builder.build();
    }
}