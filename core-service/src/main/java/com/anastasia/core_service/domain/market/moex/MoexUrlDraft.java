package com.anastasia.core_service.domain.market.moex;

import com.anastasia.core_service.utility.GetRequestParametersBuilder;
import java.util.Map;

public class MoexUrlDraft {

    private final static char delimiter = '/';
    private final static String HISTORY = "history";
    private final static String ENGINES = "engines";
    private final static String MARKETS = "markets";
    private final static String BOARDS = "boards";
    private final static String SECURITIES = "securities";
    private final static String STATISTICS = "statistics";

    private final StringBuilder uriConstructor;


    public MoexUrlDraft() {
        this.uriConstructor = new StringBuilder();
    }


    public MoexUrlDraft initiate() {
        uriConstructor.setLength(0);
        return this;
    }

    public MoexUrlDraft history() {
        uriConstructor.append(delimiter).append(HISTORY);
        return this;
    }

    public MoexUrlDraft engines() {
        uriConstructor.append(delimiter).append(ENGINES);
        return this;
    }

    public MoexUrlDraft engine(Engine engine) {
        uriConstructor.append(delimiter).append(engine);
        return this;
    }

    public MoexUrlDraft markets() {
        uriConstructor.append(delimiter).append(MARKETS);
        return this;
    }

    public MoexUrlDraft market(Market market) {
        uriConstructor.append(delimiter).append(market);
        return this;
    }

    public MoexUrlDraft boards() {
        uriConstructor.append(delimiter).append(BOARDS);
        return this;
    }

    public MoexUrlDraft board(Board board) {
        uriConstructor.append(delimiter).append(board);
        return this;
    }

    public MoexUrlDraft securities() {
        uriConstructor.append(delimiter).append(SECURITIES);
        return this;
    }

    public MoexUrlDraft statistics() {
        uriConstructor.append(delimiter).append(STATISTICS);
        return this;
    }

    public MoexUrlDraft parameterFormat() {
        uriConstructor.append(delimiter).append("%s");
        return this;
    }

    public String build() {
        String result = uriConstructor.toString();
        uriConstructor.setLength(0);
        return result;
    }

    public String build(Map<String, Object> parameters) {
        GetRequestParametersBuilder query = new GetRequestParametersBuilder(uriConstructor.toString());
        for (Map.Entry<String, Object> e : parameters.entrySet()) {
            query.add(e.getKey(), e.getValue());
        }
        uriConstructor.setLength(0);
        return query.build();
    }


    public enum Engine {

        stock, currency, futures, commodity
    }

    public enum Market {

        shares, bonds, index, foreignshares,
        selt, otc,
        forts, options,
        futures
    }

    public enum Board {
        tqbr, tqtf
    }
}
