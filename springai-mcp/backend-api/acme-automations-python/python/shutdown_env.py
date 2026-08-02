import subprocess

# Lista de diretórios contendo os arquivos docker-compose.yml
compose_dirs = [
    "../../acme-db/docker/",
    "../../acme-api/docker/",
    "../../acme-app/docker/",
    #"../../acme-infra/git/docker/",
    #"../../acme-infra/jira/docker/",
    #"../../acme-infra/oauth/docker/",
    #"../../acme-infra/sonarqube/docker/"
]

# Comando base para rodar o Docker Compose
command = ["docker-compose", "stop"]

# Loop para iniciar os serviços de cada projeto
for directory in compose_dirs:
    print(f"Stoping Docker Compose in {directory}...")
    subprocess.run(command, cwd=directory, check=True)

print("All Docker Compose services Stoping successfully!")
