# Conversor de Moedas (Java + API em tempo real)

Projeto de console em **Java 11+** que consome a **ExchangeRate-API v6** para buscar taxas **dinâmicas** de câmbio e realizar conversões entre moedas.  
Inclui **menu interativo** no terminal, **parse de JSON com Gson**, e **lógica de conversão** genérica a partir de uma moeda base.

> Repositório: https://github.com/JazzGoncalves/conversor

---

## ✨ Funcionalidades

- Busca de taxas em **tempo real** via HTTP (`HttpClient` / `HttpRequest` / `HttpResponse`)
- Parse de JSON com **Gson**
- **Menu interativo** (console) com **no mínimo 6 opções** de conversão
- **Conversão livre** entre moedas suportadas
- (Opcional) **Histórico** de conversões na sessão

**Moedas suportadas no desafio**:  
`ARS`, `BOB`, `BRL`, `CLP`, `COP`, `USD`

---

## 🧰 Tecnologias

- **Java** 11+
- **Maven**
- **Gson** (JSON)
- **JUnit 5** (testes)
- **ExchangeRate-API v6**

---

## 📦 Estrutura do projeto

```
conversor/
├─ pom.xml
└─ src/
   ├─ main/java/com/jazz/conversor/
   │  ├─ App.java                # Menu (Scanner) e orquestração
   │  ├─ ExchangeRateClient.java # HTTP + Gson (consumo da API)
   │  ├─ RatesResponse.java      # Modelo do JSON (conversion_rates)
   │  ├─ CurrencyConverter.java  # Lógica de conversão genérica
   │  ├─ CurrencyCodes.java      # Enum das moedas do desafio
   │  └─ ConversionHistory.java  # Histórico em memória

```

---

## 🔑 Configuração da API Key

Este projeto usa a **ExchangeRate-API v6**. Defina sua **API key** na variável de ambiente `EXCHANGE_API_KEY`.

### IntelliJ IDEA
1. **Run → Edit Configurations…**
2. Selecione a configuração **Application → App** (ou crie uma nova apontando para `com.jazz.conversor.App`)
3. Em **Environment variables**, adicione:
   - **Name:** `EXCHANGE_API_KEY`
   - **Value:** `sua_chave_aqui`
4. **Apply** → **OK**

### Terminal (Windows PowerShell)
```powershell
$env:EXCHANGE_API_KEY="sua_chave_aqui"
mvn -q clean compile exec:java -Dexec.mainClass="com.jazz.conversor.App"
```

### Terminal (Linux/macOS)
```bash
export EXCHANGE_API_KEY="sua_chave_aqui"
mvn -q clean compile exec:java -Dexec.mainClass="com.jazz.conversor.App"
```

> Por padrão, o client consulta:  
> `https://v6.exchangerate-api.com/v6/$EXCHANGE_API_KEY/latest/USD`

---

## ▶️ Como executar

### IntelliJ (recomendado)
- Abra `App.java` → clique no **play** ao lado do `main`.
- O menu aparecerá no console do IntelliJ.

### Maven (CLI)
Se quiser rodar via Maven (linha de comando), adicione o plugin de execução no `pom.xml` (opcional):
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>3.3.0</version>
    </plugin>
  </plugins>
</build>
```
Depois:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.jazz.conversor.App"
```

---

## 🧭 Uso (menu)

Exemplo de opções (podem variar conforme sua versão):

```
Menu:
 1) USD -> BRL
 2) BRL -> USD
 3) BRL -> ARS
 4) ARS -> BRL
 5) BRL -> CLP
 6) CLP -> BRL
 7) Conversão livre (entre as 6 moedas)
 8) Histórico (sessão)
 9) Sair
Selecione uma opção: 2
Valor a converter: 100
Base: USD | Atualizado em: Mon, 18 Aug 2025 00:00:01 +0000
Resultado: 100.0000 BRL -> 19.1234 USD
```

---

## 🧠 Como funciona a conversão

A API retorna `conversion_rates` **relativos à moeda base (USD)**.  
Para **converter de `from` para `to`**:

1. Converta para a base: `valorNaBase = amount / rate(from)`
2. Converta da base para o destino: `resultado = valorNaBase * rate(to)`

Quando `from` = base, é só `amount * rate(to)`.  
Quando `to` = base, é só `amount / rate(from)`.

---

## ✅ Requisitos da atividade (checklist)

- [x] Interação textual (console) com menu e 6+ opções  
- [x] Taxas **dinâmicas** via **API**  
- [x] Requisições HTTP com **HttpClient/HttpRequest/HttpResponse**  
- [x] Parse de JSON com **Gson**  
- [x] Filtro de moedas no **mapa** de taxas  
- [x] Lógica de conversão implementada  
- [x] Histórico de conversões  
- [x] Projeto pronto para **IntelliJ** e **Maven**

---

## 🛠️ Troubleshooting

- **`package com.google.gson does not exist`**  
  → Falta dependência no `pom.xml` ou Maven não recarregado.  
  **Solução:** confirmar `gson` no `pom.xml` e clicar em **Reload All Maven Projects**.

- **`Defina EXCHANGE_API_KEY`**  
  → Variável não configurada.  
  **Solução:** ver seção **Configuração da API Key**.

- **HTTP 4xx/5xx**  
  → Chave inválida, quota excedida ou instabilidade do serviço.  
  **Solução:** conferir chave, plano da API e tentar novamente.

---

## 📈 Ideias de evolução

- Cache das taxas por sessão (evitar chamar a API a cada conversão)
- Persistir histórico em arquivo (`.csv`/`.json`)
- Logs com SLF4J/Logback
- Mais moedas, validações e testes
- Internacionalização (mensagens em EN/ES)

---
