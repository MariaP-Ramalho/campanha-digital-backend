# Campanha Digital 📊 

Bem-vindo à Campanha Digital!
Um sistema desenvolvido para **analisar o desempenho de lives no YouTube** por meio de **Inteligência Artificial** aplicada à categorização e análise de comentários. 

## 🎯 **Objetivo**

### 🔎 **Coleta de Dados** 
O sistema captura e armazena todos os comentários das lives a partir do início da análise, registrando informações como:  
- ⏱ **Timestamp** do comentário  
- 👤 **Nome do usuário**  
- 🖼 **Foto do canal**  
- 🔗 **Link para o canal**  
- 💬 **Tipo de interação**  
- 😀😡 **Sentimento identificado pela IA (OpenAI)**  

### 📈 Geração de Dashboards  
Com base nesses dados, o sistema gera **dashboards interativos** que permitem extrair insights valiosos.  
Um exemplo é o **Dashboard de Timeline**, que mostra em quais momentos da live houve aumento de comentários **positivos** ou **negativos**, ajudando a identificar quais acontecimentos provocaram maior reação do público.  

### 🧮 Possibilidades de Uso  
A análise pode ser feita em **qualquer live pública do YouTube**, incluindo transmissões de concorrentes.  
Isso possibilita:  
- Comparar desempenho entre diferentes canais ou eventos  
- Identificar momentos de maior **aprovação** e **desaprovação**  
- Usar os insights de forma **estratégica**, como em campanhas políticas ou ações de marketing digital  


---

## 📦 **Dependências**

Antes de rodar o projeto, verifique se as seguintes dependências estão instaladas:

- **PostgreSQL**: É necessário ter o **PostgreSQL** instalado e rodando localmente para que o banco de dados seja configurado corretamente.

---

## ▶️ Criação da Chave da API do YouTube

Para utilizar a **YouTube Data API v3**, é necessário gerar uma chave de API no **Google Cloud Console**.

### Passo a passo

1. **Acesse o Google Cloud Console**  
   - [console.cloud.google.com](https://console.cloud.google.com/)  
   - Crie uma conta caso ainda não possua

2. **Crie um novo projeto**  
   - No painel inicial clique em **Criar Projeto**  
   - Defina um nome e confirme a criação

3. **Ative a API do YouTube**  
   - Vá em **APIs e serviços > Biblioteca**  
   - Pesquise por **YouTube Data API v3**  
   - Clique em **Ativar**

4. **Crie as credenciais da API**  
   - Acesse **APIs e serviços > Credenciais**  
   - Clique em **Criar credenciais > Chave de API**  
   - Copie a chave gerada e guarde em local seguro

🔗 Guia completo: [Tutorial](https://suporte.presence.com.br/portal/pt/kb/articles/criando-uma-chave-para-a-api-de-dados-do-youtube)

--- 

## 🤖 Criação da Chave da API da OpenIa

1. **Crie uma Conta na OpenAI**
   - Acesse o site da [OpenAI](https://openai.com/pt-BR/).
   - Clique em “Sign Up” no canto superior direito da página.
   -Preencha os campos necessários com suas informações ou faça login utilizando sua conta do Google.

3. **Acesse a Área de API Keys**
   - No painel principal da OpenAI, clique em “API” no menu lateral esquerdo.
   - Em seguida, clique em “API Keys”.
  
3. **Crie uma Nova API Key**
   - Clique no botão “Create new secret key”.
   - Dê um nome para a sua chave, algo que ajude você a identificá-la no futuro, como “API do curso”.
   - Clique em “Create secret key”.
  
4. **Copie e Guarde sua API Key**
Após criar a chave, a OpenAI exibirá o valor da sua API Key. É crucial que você copie e guarde esse valor em um lugar seguro, pois ele só será exibido uma vez. Se você perder essa chave, será necessário criar uma nova.

> ⚠️ **Observação**  
> A API da OpenAI é um serviço pago.  
> Caso você esteja avaliando este projeto para fins de recrutamento e deseje testá-lo em funcionamento, poderá solicitar uma chave temporária diretamente comigo pelo e-mail: **eduardapramalho@gmail.com**

---

## 📁 Configuração de ambiente

É necessário um arquivo `.env` na raiz do projeto com a seguinte estrutura: 

```declarative
OPENAI_API_KEY=sua_chave_openIA
YOUTUBE_API_KEY=sua_chave_youtube
```

---

## 🗄️ **Configuração do Banco de Dados - PostgreSQL**

1. **Crie um banco de dados com o seguinte nome:**

```bash
campanha-digital
```

2. **Configurações do banco de dados:**

- **Usuário (superusuário):** `postgres`
- **Senha:** `postgres`
- **Porta:** 5432

Obs.: essas são as configurações definidas no arquivo `src/main/resources/application.yml`

Certifique-se de que o PostgreSQL esteja rodando corretamente em sua máquina local para que o backend se conecte ao banco de dados.

---

## 🚀 **Rodando o Projeto no Terminal**

Com as dependências configuradas, vamos rodar o projeto. Para isso, execute o seguinte comando:

```bash
mvn clean spring-boot:run
```

Obs.: O `clean` é opcional, mas recomendado caso precise limpar o build e começar de novo.

---

## 🔧 Acessando a API - Swagger UI
Agora que o BackEnd está rodando, você pode visualizar e testar a API diretamente no Swagger UI!

Acesse em:
http://localhost:8090/swagger-ui.html

## FrontEnd
Você pode acessar o frontEnd através do link a seguir: https://github.com/MariaP-Ramalho/campanha-digital-frontend
