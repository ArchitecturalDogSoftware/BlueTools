package net.architecturaldog.bluetools.content.material;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class SimpleMaterialMiningLevel extends MaterialMiningLevel {

    private final List<Rule> rules;

    public SimpleMaterialMiningLevel(final List<Rule> rules) {
        this.rules = ImmutableList.copyOf(rules);
    }

    @Override
    public List<Rule> getRules() {
        return this.rules;
    }

}
