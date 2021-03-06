package io.github.mat3e.odata.common.provider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.provider.CsdlActionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlFunctionImport;
import org.testng.annotations.Test;

import io.github.mat3e.odata.common.annotation.ODataEntity;
import io.github.mat3e.odata.common.annotation.ODataKey;
import io.github.mat3e.odata.common.annotation.ODataProperty;
import io.github.mat3e.odata.common.entity.JpaOlingoEntity;
import io.github.mat3e.odata.common.exception.CsdlExtractException;
import io.github.mat3e.odata.common.provider.csdl.JpaEntityCsdlProvider;

public class AbstractEdmProviderTest {

    private final String SET = "TestEntities";

    class TestEdmProvider extends AbstractEdmProvider {

        TestEdmProvider() throws CsdlExtractException {
            super(Stream.of(new TestCsdlEntityProvider()).collect(Collectors.toList()));
        }

        @Override
        protected List<CsdlActionImport> getActionImports() {
            return Collections.emptyList();
        }

        @Override
        protected List<CsdlFunctionImport> getFunctionImports() {
            return Collections.emptyList();
        }
    }

    @ODataEntity(name = "TestEntity", entitySetName = SET)
    class TestEntity extends JpaOlingoEntity {
        @ODataKey
        @ODataProperty(name = "ID", type = EdmPrimitiveTypeKind.String)
        private String ID = "1";

        @ODataProperty(name = "Name", type = EdmPrimitiveTypeKind.String)
        private String name = "name";

        public String getID() {
            return this.ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class TestCsdlEntityProvider extends JpaEntityCsdlProvider<TestEntity> {
        TestCsdlEntityProvider() throws CsdlExtractException {
            super(TestEntity.class);
        }
    }

    @Test
    public void test_AbstractEdmProvider_isCreatedProperly() throws CsdlExtractException {

        // GIVEN + WHEN
        final TestEdmProvider sut = new TestEdmProvider();

        // THEN
        assertThat(sut.getSchemas()).hasSize(5); // entity, action, function, enum, complex type
        assertThat(sut.getEntityContainer().getEntitySets()).hasSize(1);
        assertThat(sut.getEntityContainer().getEntitySets().get(0).getName()).isEqualTo(SET);
        assertThat(sut.getActionImports()).hasSize(0);
        assertThat(sut.getFunctionImports()).hasSize(0);
    }
}
