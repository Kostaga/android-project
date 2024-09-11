package com.example.habit_app.logic.repository;

import android.os.AsyncTask;

import com.example.habit_app.data.models.Character;
import com.example.habit_app.logic.dao.CharacterDao;

import java.util.List;

public class CharacterRepository {

    private final CharacterDao characterDao;

    public CharacterRepository(CharacterDao characterDao) {
        this.characterDao = characterDao;
    }
    
    public void increaseHp(int characterId, int amount) {
        new IncreaseHpAsyncTask(characterDao, characterId, amount).execute();
    }



    public void decreaseHp(int characterId, int amount) {
        new DecreaseHpAsyncTask(characterDao, characterId, amount).execute();
    }

    public void increaseXp(int characterId, int amount) {
        new IncreaseXpAsyncTask(characterDao, characterId, amount).execute();
    }

    public void increaseLevel(int characterId, int amount) {
        new IncreaseLevelAsyncTask(characterDao, characterId, amount).execute();
    }

    public void increaseCoins(int characterId, int amount) {
        new IncreaseCoinsAsyncTask(characterDao, characterId, amount).execute();
    }

    public void decreaseCoins(int characterId, int amount) {
        new DecreaseCoinsAsyncTask(characterDao, characterId, amount).execute();
    }


    public void addCharacter(Character character) {
        new AddCharacterAsyncTask(characterDao).execute(character);
    }

    public void updateCharacter(Character character) {
        new UpdateCharacterAsyncTask(characterDao).execute(character);
    }



    private static class IncreaseHpAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CharacterDao characterDao;
        private final int characterId;
        private final int amount;

        private IncreaseHpAsyncTask(CharacterDao characterDao, int characterId, int amount) {
            this.characterDao = characterDao;
            this.characterId = characterId;
            this.amount = amount;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterDao.increaseHp(characterId, amount);
            return null;
        }
    }

    private static class DecreaseHpAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CharacterDao characterDao;
        private final int characterId;
        private final int amount;

        private DecreaseHpAsyncTask(CharacterDao characterDao, int characterId, int amount) {
            this.characterDao = characterDao;
            this.characterId = characterId;
            this.amount = amount;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterDao.decreaseHp(characterId, amount);
            return null;
        }
    }


    private static class IncreaseXpAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CharacterDao characterDao;
        private final int characterId;
        private final int amount;

        private IncreaseXpAsyncTask(CharacterDao characterDao, int characterId, int amount) {
            this.characterDao = characterDao;
            this.characterId = characterId;
            this.amount = amount;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterDao.increaseXp(characterId, amount);
            return null;
        }
    }

    private static class IncreaseLevelAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CharacterDao characterDao;
        private final int characterId;
        private final int amount;

        private IncreaseLevelAsyncTask(CharacterDao characterDao, int characterId, int amount) {
            this.characterDao = characterDao;
            this.characterId = characterId;
            this.amount = amount;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterDao.increaseLevel(characterId, amount);
            return null;
        }
    }


    private static class IncreaseCoinsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CharacterDao characterDao;
        private final int characterId;
        private final int amount;

        private IncreaseCoinsAsyncTask(CharacterDao characterDao, int characterId, int amount) {
            this.characterDao = characterDao;
            this.characterId = characterId;
            this.amount = amount;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterDao.increaseCoins(characterId, amount);
            return null;
        }
    }

    private static class DecreaseCoinsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CharacterDao characterDao;
        private final int characterId;
        private final int amount;

        private DecreaseCoinsAsyncTask(CharacterDao characterDao, int characterId, int amount) {
            this.characterDao = characterDao;
            this.characterId = characterId;
            this.amount = amount;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterDao.decreaseCoins(characterId, amount);
            return null;
        }
    }

    private static class AddCharacterAsyncTask extends AsyncTask<Character, Void, Void> {
        private final CharacterDao characterDao;

        private AddCharacterAsyncTask(CharacterDao characterDao) {
            this.characterDao = characterDao;
        }

        @Override
        protected Void doInBackground(Character... characters) {
            characterDao.addCharacter(characters[0]);
            return null;
        }
    }

    private static class UpdateCharacterAsyncTask extends AsyncTask<Character, Void, Void> {
        private CharacterDao characterDao;

        private UpdateCharacterAsyncTask(CharacterDao characterDao) {
            this.characterDao = characterDao;
        }

        @Override
        protected Void doInBackground(Character... characters) {
            characterDao.updateCharacter(characters[0]);
            return null;
        }
    }

    private static class DeleteCharacterAsyncTask extends AsyncTask<Character, Void, Void> {
        private CharacterDao characterDao;

        private DeleteCharacterAsyncTask(CharacterDao characterDao) {
            this.characterDao = characterDao;
        }

        @Override
        protected Void doInBackground(Character... characters) {
            characterDao.deleteCharacter(characters[0]);
            return null;
        }
    }

    private static class DeleteAllCharactersAsyncTask extends AsyncTask<Void, Void, Void> {
        private CharacterDao characterDao;

        private DeleteAllCharactersAsyncTask(CharacterDao characterDao) {
            this.characterDao = characterDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterDao.deleteAllCharacters();
            return null;
        }
    }
}
