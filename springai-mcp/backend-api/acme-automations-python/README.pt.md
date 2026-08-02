# Automações Acme em Python

Este projeto contém uma coleção de scripts Python para automatizar diversas tarefas relacionadas ao projeto Acme, como gerenciar serviços Docker, construir projetos e executar ambientes.

## Estrutura do Projeto

### Visão Geral dos Scripts

#### `build_acme_api.py`
- **Descrição**: Script de exemplo para construir o projeto `acme-api`. Os detalhes da implementação não foram fornecidos no contexto atual.

#### `run_env.py`
- **Descrição**: Orquestra a execução de múltiplos scripts em uma sequência específica para configurar o ambiente.
- **Principais Funcionalidades**:
  - Executa scripts como iniciar serviços Docker, criar banco de dados e construir projetos.
  - Inclui pausas entre as etapas para maior controle.
  - Fornece logs detalhados para cada etapa, incluindo mensagens de sucesso ou falha.

#### `run_acme_db_maven.py`
- **Descrição**: Script de exemplo para executar tarefas de criação de banco de dados usando Maven. Os detalhes da implementação não foram fornecidos no contexto atual.

#### `shutdown_env.py`
- **Descrição**: Encerra o ambiente executando scripts específicos.
- **Principais Funcionalidades**:
  - Executa o script `stop_docker_services.py` para parar todos os serviços Docker.
  - Fornece tratamento de erros e registra a saída dos scripts executados.

#### `start_docker_services_api.py`
- **Descrição**: Inicia os serviços Docker para o projeto `acme-api`.
- **Principais Funcionalidades**:
  - Usa `docker-compose` para iniciar os serviços em modo desacoplado.
  - Aponta para o diretório `../../acme-api/docker/`.

#### `start_docker_services_app.py`
- **Descrição**: Inicia os serviços Docker para o projeto `acme-app`.
- **Principais Funcionalidades**:
  - Usa `docker-compose` para iniciar os serviços em modo desacoplado.
  - Aponta para o diretório `../../acme-app/docker/`.

#### `start_docker_services_db.py`
- **Descrição**: Inicia os serviços Docker para o projeto `acme-db`.
- **Principais Funcionalidades**:
  - Usa `docker-compose` para iniciar os serviços em modo desacoplado.
  - Aponta para o diretório `../../acme-db/docker/`.

#### `stop_docker_services.py`
- **Descrição**: Para todos os serviços Docker em execução para os projetos especificados.
- **Principais Funcionalidades**:
  - Usa `docker-compose` para parar os serviços.
  - Aponta para diretórios como `../../acme-db/docker/`.

## Como Usar

1. Clone o repositório para sua máquina local.
2. Navegue até o diretório `python/`.
3. Execute o script desejado usando Python:
   ```bash
   python <script_name>.py
   ```
4. Acompanhe os logs para monitorar a execução.

## Notas

- Certifique-se de que Docker e Docker Compose estão instalados e configurados no seu sistema.
- Atualize as listas `scripts_to_run` no arquivo run_env.py nos scripts para incluir ou excluir serviços específicos conforme necessário.