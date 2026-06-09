# AEGIS — Plataforma de Monitoramento de Detritos Espaciais e Proteção Orbital

Este repositório contém o código-fonte do back-end da plataforma **AEGIS**, uma API RESTful de alta performance desenvolvida com **Java** e **Spring Boot**. O objetivo do sistema é mitigar os impactos do lixo espacial na órbita terrestre, protegendo satélites comerciais e auxiliando na gestão de missões autônomas de limpeza realizadas por drones interceptadores (*Chasers*).

A solução conecta diretamente a exploração e segurança espacial a oportunidades reais na Terra, mitigando desastres tecnológicos e econômicos em conformidade com as diretrizes da **Economia Espacial** estabelecidas para a Global Solution 2026/1.

---

## 🚀 Links do Projeto (Centralização Obrigatória)

Para atender estritamente aos critérios de avaliação e organização exigidos no edital da Global Solution, todos os artefatos de entrega e links públicos de acesso estão centralizados abaixo:

* **Link do Deploy Público:** `https://aegisglobalsolution.onrender.com`
* **Link do Vídeo de Apresentação:** `https://www.youtube.com/watch?v=[INSIRA-AQUI-O-ID-DO-SEU-VIDEO]`
* **Link do Vídeo Pitch:** `https://www.youtube.com/watch?v=[INSIRA-AQUI-O-ID-DO-PITCH]`
* **Documentação Interativa da API (Swagger UI):** `https://aegisglobalsolution.onrender.com/swagger-ui/index.html` (ou local em `http://localhost:8080/swagger-ui/index.html`)

---

## 🌍 Alinhamento com os Objetivos de Desenvolvimento Sustentável (ODS) da ONU

A plataforma AEGIS foi concebida para atuar diretamente no suporte e manutenção das metas globais de sustentabilidade, integrando os seguintes objetivos:
* **ODS 9 — Indústria, Inovação e Infraestrutura:** Fomento à infraestrutura aeroespacial resiliente por meio de sistemas inteligentes e preventivos contra colisões.
* **ODS 11 — Cidades e Comunidades Sustentáveis:** Proteção dos serviços de telecomunicação, previsão de desastres ambientais, clima e agronegócio terrestre que dependem diretamente da estabilidade orbital.
* **ODS 13 — Ação Contra a Mudança Global do Clima:** Manutenção operacional da frota de satélites meteorológicos essenciais para o monitoramento ativo do aquecimento global e desmatamento.

---

## 🛠️ Tecnologias e Frameworks Utilizados

* **Linguagem Principal:** Java 21
* **Framework Base:** Spring Boot
* **Mecanismo de Persistência:** Spring Data JPA / Hibernate
* **Segurança da Informação:** Spring Security & Autenticação Baseada em Tokens JWT (JSON Web Tokens)
* **Banco de Dados:** Oracle SQL 
* **Documentação Automática:** OpenAPI 3.0 / Swagger UI
* **Produtividade & Organização:** Lombok, Jakarta Validation (@Valid), Spring Boot DevTools
* **Hipermídia & Maturidade REST:** Spring HATEOAS
* **Execução Assíncrona e Automação:** Spring Task Scheduling (`@EnableScheduling`)

---

## 🏛️ Arquitetura do Software e Padrões de Design

O projeto adota o padrão de arquitetura monolítica organizada rigorosamente em camadas de responsabilidade isoladas, garantindo desacoplamento, testabilidade e fácil manutenção:
1. **Model / Entity:** Representação física das tabelas relacionais do banco de dados, utilizando mapeamentos avançados do JPA (Herança, Chaves Compostas e Objetos Embutidos).
2. **Repository:** Camada de persistência que estende `JpaRepository`, aproveitando o poder das *Derived Queries* e paginação em nível de consulta.
3. **DTO (Data Transfer Objects):** Implementado por meio de **Java Records** para garantir a imutabilidade absoluta no tráfego de dados entre o controlador e os serviços.
4. **Service:** Centralização de 100% das regras de negócio do ecossistema planetário (cálculos de consumo de bateria, validações de status operacionais, registros de logs).
5. **Controller:** Camada de exposição dos Endpoints RESTful, responsável por gerenciar os verbos HTTP, códigos de status adequados e injeção de hipermídia (HATEOAS).

### Mapeamento de Modelagem Avançada Implementado
* **Embedded (`@Embedded` / `@Embeddable`):** Aplicado na classe `CoordenadaOrbital` dentro das entidades `Satelite` e `DetritoEspacial`, permitindo o reuso dos eixos cartesianos (X, Y, Altitude) sem duplicidade estrutural na base.
* **Chave Composta (`@EmbeddedId`):** Implementado na entidade associativa `MissaoIntercepcao` através do record/classe `MissaoId`, unindo o `drone_id` e o `detrito_id`.
* **Relacionamentos Avançados:** Implementações estritas de relacionamentos de `1:N` (Empresa -> Satélites) e `N:N` com atributos extras tratados por meio da entidade customizada de Missão.

---

## ⚡ Recursos Diferenciais Prontos para Produção

### 1. Paginação Nativa e Universal
Todas as rotas de listagem (`GET`) da API utilizam paginação estruturada via query params (`page` e `size`). Isso otimiza o tráfego de rede e preserva a memória do aplicativo mobile desenvolvido em **React Native**, impedindo travamentos ao renderizar grandes volumes de dados.

### 2. Tratamento Global de Exceções (`@RestControllerAdvice`)
A API intercepta centralizadamente qualquer erro do sistema (recurso não encontrado, violação de regras de negócio ou falha de validação de campos) e responde de forma padronizada em formato JSON utilizando o formato internacional `ErroPadraoDTO`, mapeando: `timestamp`, `status`, `message`, `path` e `details`.

### 3. Simulador em Tempo Real (`@Scheduled`)
A rota de drones possui uma rotina automatizada em segundo plano que executa a cada 20 segundos. Ela simula o comportamento físico dos drones no espaço: altera estados operacionais de `INTERCEPTANDO` para `RETORNANDO` e gerencia a recarga gradual da bateria até 100%, gerando histórico automático no painel de logs.

### 4. Database Seeder Automático
Para facilitar os testes rápidos da banca avaliadora e a conteinerização em Docker, a API executa um script de inicialização de dados (`CommandLineRunner`) que injeta perfis de acesso padrão (`engenheiro@aegis.com` e `operador@aegis.com`) e unidades da frota de drones imediatamente na primeira execução da aplicação.

---

## 📋 Catálogo Base de Endpoints da API

### Autenticação (Público)
* `POST /api/auth/register` - Cadastro de novos usuários operadores.
* `POST /api/auth/login` - Autenticação com e-mail e senha, retornando o token JWT.

### Perfil e Usuários (Protegido por Token JWT)
* `GET /api/usuarios` - Listagem paginada de operadores cadastrados (Apenas perfis ADMIN).
* `GET /api/usuarios/{id}` - Detalhamento do perfil.
* `PUT /api/usuarios/{id}` - Edição de dados cadastrais e senha.
* `DELETE /api/usuarios/{id}` - Encerramento e exclusão de conta.

### Empresas Aeroespaciais (Protegido por Token JWT)
* `GET /api/empresas` - Listagem do catálogo completo de empresas com links HATEOAS.
* `GET /api/empresas/{id}` - Detalhamento de uma empresa específica.
* `POST /api/empresas` - Registro de uma nova corporação aeroespacial.
* `PUT /api/empresas/{id}` - Atualização de dados cadastrais (ex: Nome, CNPJ).
* `DELETE /api/empresas/{id}` - Remoção definitiva da empresa e de seus satélites vinculados.

### Satélites Comerciais (Protegido por Token JWT)
* `GET /api/satelites` - Listagem paginada de satélites com links HATEOAS.
* `GET /api/satelites/{id}` - Telemetria completa do satélite selecionado.
* `POST /api/satelites` - Registro e lançamento de um novo satélite comercial.
* `PUT /api/satelites/{id}` - Atualização de parâmetros orbitais (Botão Editar do App).
* `DELETE /api/satelites/{id}` - Desativação e remoção física do satélite monitorado.

### Frota de Drones Limpadores (Protegido por Token JWT)
* `GET /api/drones` - Listagem paginada de status da frota.
* `GET /api/drones/{id}` - Consulta individual de níveis de bateria e status operacional.
* `POST /api/drones` - Fabricação de uma nova unidade interceptadora.
* `PUT /api/drones/{id}` - Alteração de designação da unidade.
* `DELETE /api/drones/{id}` - Baixa e desativação de drones avariados.

### Radar de Detritos e Missões (Protegido por Token JWT)
* `GET /api/detritos` - Listagem paginada de lixo orbital monitorado.
* `POST /api/detritos` - Catalogação de nova ameaça no radar.
* `POST /api/missoes/despachar` - Despacha dinamicamente um drone disponível, calculando o custo de bateria com base no risco do detrito (`BAIXO`, `MODERADO`, `ALTO`, `CRITICO`).

### Painéis Gerenciais
* `GET /api/dashboard` - Consolidação estatística em tempo real da Saúde Orbital, alertas e contagem de riscos para alimentação de gráficos.
* `GET /api/logs` - Timeline cronológica paginada de auditoria da segurança espacial.

---

## ⚙️ Instruções de Instalação e Execução (How-To)

### Pré-requisitos Técnicos
* Java Development Kit (JDK) 17 ou superior instalado.
* Apache Maven 3.8+ configurado nas variáveis de ambiente.
* Banco de Dados (Oracle DB configurado na porta padrão, ou altere o profile para o banco em memória H2 no arquivo `application.properties`).

### Passo a Passo para Execução Local

1. **Clonar o Repositório do GitHub:**
   ```bash
   git clone https://github.com/LucasMatsubara/javaGlobalSolution.git
   cd aegis-api
   ```

2. **Configurar as Credenciais do Banco de Dados:**
   Abra o arquivo `src/main/resources/application.properties` e verifique as linhas de conexão:
   ```properties
   spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```
   *(Nota: Se desejar testar sem instalar o Oracle localmente, altere as configurações do datasource para o driver e URL do H2 Database).*

3. **Compilar o Projeto com o Maven:**
   ```bash
   mvn clean compile
   ```

4. **Executar a Aplicação:**
   ```bash
   mvn spring-boot:run
   ```

5. **Acessar a Plataforma:**
   * A API estará operando no endereço: `http://localhost:8080`
   * Para testar interativamente as rotas pelo Swagger, abra no navegador: `http://localhost:8080/swagger-ui/index.html`

---

## 👥 Integrantes do Grupo Turma 2TDSPX

* João Pedro Pereira Camilo        | RM: 562005
* Lucas Matsubara Reis             | RM: 565020
* Pamella Christiny Chaves Brito   | RM: 565206
