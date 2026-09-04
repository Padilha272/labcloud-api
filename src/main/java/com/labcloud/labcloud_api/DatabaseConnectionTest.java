package com.labcloud.labcloud_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    private final DataSource dataSource;

    public DatabaseConnectionTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            System.out.println("✅ Conexão com PostgreSQL estabelecida!");
            System.out.println("📊 Database: " + connection.getCatalog());
            System.out.println("👤 Usuário: " + metaData.getUserName());
            System.out.println("📍 URL: " + metaData.getURL());
            System.out.println("🔧 Driver: " + metaData.getDriverName());
            System.out.println("📦 Versão: " + metaData.getDatabaseProductVersion());
        } catch (Exception e) {
            System.err.println("❌ Erro ao conectar: " + e.getMessage());
            System.err.println("💡 Verifique se o PostgreSQL está rodando");
        }
    }
}