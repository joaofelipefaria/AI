# Automatisations Acme en Python

Ce projet contient une collection de scripts Python pour automatiser diverses tâches liées au projet Acme, telles que la gestion des services Docker, la construction de projets et l'exécution d'environnements.

## Structure du projet

### Aperçu des scripts

#### `build_acme_api.py`
- **Description** : Script de remplacement pour la construction du projet `acme-api`. Les détails de l'implémentation ne sont pas fournis dans le contexte actuel.

#### `run_env.py`
- **Description** : Orchestre l'exécution de plusieurs scripts dans une séquence spécifique pour configurer l'environnement.
- **Caractéristiques principales** :
  - Exécute des scripts comme le démarrage des services Docker, la création de bases de données et la construction de projets.
  - Inclut des pauses de latence entre les étapes pour un meilleur contrôle.
  - Fournit des journaux détaillés pour chaque étape, y compris les messages de succès ou d'échec.

#### `run_acme_db_maven.py`
- **Description** : Script de remplacement pour exécuter des tâches de création de bases de données à l'aide de Maven. Les détails de l'implémentation ne sont pas fournis dans le contexte actuel.

#### `shutdown_env.py`
- **Description** : Arrête l'environnement en exécutant des scripts spécifiques.
- **Caractéristiques principales** :
  - Exécute le script `stop_docker_services.py` pour arrêter tous les services Docker.
  - Fournit une gestion des erreurs et enregistre la sortie des scripts exécutés.

#### `start_docker_services_api.py`
- **Description** : Démarre les services Docker pour le projet `acme-api`.
- **Caractéristiques principales** :
  - Utilise `docker-compose` pour démarrer les services en mode détaché.
  - Cible le répertoire `../../acme-api/docker/`.

#### `start_docker_services_app.py`
- **Description** : Démarre les services Docker pour le projet `acme-app`.
- **Caractéristiques principales** :
  - Utilise `docker-compose` pour démarrer les services en mode détaché.
  - Cible le répertoire `../../acme-app/docker/`.

#### `start_docker_services_db.py`
- **Description** : Démarre les services Docker pour le projet `acme-db`.
- **Caractéristiques principales** :
  - Utilise `docker-compose` pour démarrer les services en mode détaché.
  - Cible le répertoire `../../acme-db/docker/`.

#### `stop_docker_services.py`
- **Description** : Arrête tous les services Docker en cours d'exécution pour les projets spécifiés.
- **Caractéristiques principales** :
  - Utilise `docker-compose` pour arrêter les services.
  - Cible des répertoires comme `../../acme-db/docker/`.

## Comment utiliser

1. Clonez le dépôt sur votre machine locale.
2. Accédez au répertoire `python/`.
3. Exécutez le script souhaité en utilisant Python :
   ```bash
   python <script_name>.py
   ```
4. Suivez les journaux pour surveiller l'exécution.

## Notes

- Assurez-vous que Docker et Docker Compose sont installés et configurés sur votre système.
- Mettez à jour les listes `scripts_to_run` dans le fichier run_env.py dans les scripts pour inclure ou exclure des services spécifiques selon vos besoins.