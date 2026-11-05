-- Données test
--H2 génére les ids pour éviter les conflits avec IDENTITY

INSERT INTO dresseur (nom, region) VALUES ('Sacha', 'Kanto');
INSERT INTO dresseur (nom, region) VALUES ('Pierre', 'Kanto');

INSERT INTO pokemon (nom, type, niveau, dresseur_id)
VALUES ('Goupix', 'Feu', 25, (SELECT id FROM dresseur WHERE nom = 'Sacha'));
INSERT INTO pokemon (nom, type, niveau, dresseur_id)
VALUES ('Rondoudou', 'Normal', 50, (SELECT id FROM dresseur WHERE nom = 'Sacha'));
INSERT INTO pokemon (nom, type, niveau, dresseur_id)
VALUES ('Tortank', 'Eau', 80, (SELECT id FROM dresseur WHERE nom = 'Sacha'));
