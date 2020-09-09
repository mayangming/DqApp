package com.dq.im.db;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.dq.im.dao.HomeImBaseDao;
import com.dq.im.dao.HomeImBaseDao_Impl;
import com.dq.im.dao.P2PMessageBaseDao;
import com.dq.im.dao.P2PMessageBaseDao_Impl;
import com.dq.im.dao.TeamDao;
import com.dq.im.dao.TeamDao_Impl;
import com.dq.im.dao.TeamMessageBaseDao;
import com.dq.im.dao.TeamMessageBaseDao_Impl;
import com.dq.im.dao.TeamUserJoinDao;
import com.dq.im.dao.TeamUserJoinDao_Impl;
import com.dq.im.dao.UserDao;
import com.dq.im.dao.UserDao_Impl;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ImRoomDatabase_Impl extends ImRoomDatabase {
  private volatile UserDao _userDao;

  private volatile TeamDao _teamDao;

  private volatile P2PMessageBaseDao _p2PMessageBaseDao;

  private volatile TeamMessageBaseDao _teamMessageBaseDao;

  private volatile HomeImBaseDao _homeImBaseDao;

  private volatile TeamUserJoinDao _teamUserJoinDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `user_table` (`userId` TEXT NOT NULL, `username` TEXT NOT NULL, `nick` TEXT NOT NULL, `sex` TEXT NOT NULL, `picUrl` TEXT NOT NULL, `isFriend` TEXT NOT NULL, PRIMARY KEY(`userId`))");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_user_table_userId` ON `user_table` (`userId`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `team_table` (`userId` TEXT NOT NULL, `groupId` TEXT NOT NULL, `groupAvatarUrl` TEXT NOT NULL, `groupName` TEXT NOT NULL, `groupNick` TEXT NOT NULL, `createTime` TEXT NOT NULL, PRIMARY KEY(`groupId`))");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_team_table_groupId` ON `team_table` (`groupId`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `home_message` (`msgIdClient` TEXT NOT NULL, `msgIdServer` TEXT NOT NULL, `type` TEXT NOT NULL, `msgType` TEXT NOT NULL, `msgSecondType` TEXT NOT NULL, `createTime` INTEGER NOT NULL, `sourceContent` TEXT NOT NULL, `messageSendStatus` INTEGER NOT NULL, `toUserId` TEXT NOT NULL, `fromUserId` TEXT NOT NULL, `unReadNumber` INTEGER NOT NULL, `groupId` TEXT, PRIMARY KEY(`msgIdClient`, `msgIdServer`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `team_user_join` (`teamId` TEXT NOT NULL, `userId` TEXT NOT NULL, PRIMARY KEY(`teamId`, `userId`), FOREIGN KEY(`teamId`) REFERENCES `team_table`(`groupId`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`userId`) REFERENCES `user_table`(`userId`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_team_user_join_teamId` ON `team_user_join` (`teamId`)");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_team_user_join_userId` ON `team_user_join` (`userId`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `team_message` (`msgIdClient` TEXT NOT NULL, `msgIdServer` TEXT NOT NULL, `type` TEXT NOT NULL, `msgType` TEXT NOT NULL, `msgSecondType` TEXT NOT NULL, `createTime` INTEGER NOT NULL, `sourceContent` TEXT NOT NULL, `messageSendStatus` INTEGER NOT NULL, `toUserId` TEXT NOT NULL, `fromUserId` TEXT NOT NULL, `groupId` TEXT NOT NULL, PRIMARY KEY(`msgIdClient`, `msgIdServer`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `person_message` (`msgIdClient` TEXT NOT NULL, `msgIdServer` TEXT NOT NULL, `type` TEXT NOT NULL, `msgType` TEXT NOT NULL, `msgSecondType` TEXT NOT NULL, `createTime` INTEGER NOT NULL, `sourceContent` TEXT NOT NULL, `messageSendStatus` INTEGER NOT NULL, `toUserId` TEXT NOT NULL, `fromUserId` TEXT NOT NULL, PRIMARY KEY(`msgIdClient`, `msgIdServer`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1103de7e3ebedf31aeca600db0d132f5')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `user_table`");
        _db.execSQL("DROP TABLE IF EXISTS `team_table`");
        _db.execSQL("DROP TABLE IF EXISTS `home_message`");
        _db.execSQL("DROP TABLE IF EXISTS `team_user_join`");
        _db.execSQL("DROP TABLE IF EXISTS `team_message`");
        _db.execSQL("DROP TABLE IF EXISTS `person_message`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsUserTable = new HashMap<String, TableInfo.Column>(6);
        _columnsUserTable.put("userId", new TableInfo.Column("userId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("nick", new TableInfo.Column("nick", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("sex", new TableInfo.Column("sex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("picUrl", new TableInfo.Column("picUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("isFriend", new TableInfo.Column("isFriend", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserTable = new HashSet<TableInfo.Index>(1);
        _indicesUserTable.add(new TableInfo.Index("index_user_table_userId", true, Arrays.asList("userId")));
        final TableInfo _infoUserTable = new TableInfo("user_table", _columnsUserTable, _foreignKeysUserTable, _indicesUserTable);
        final TableInfo _existingUserTable = TableInfo.read(_db, "user_table");
        if (! _infoUserTable.equals(_existingUserTable)) {
          return new RoomOpenHelper.ValidationResult(false, "user_table(com.dq.im.model.UserModel).\n"
                  + " Expected:\n" + _infoUserTable + "\n"
                  + " Found:\n" + _existingUserTable);
        }
        final HashMap<String, TableInfo.Column> _columnsTeamTable = new HashMap<String, TableInfo.Column>(6);
        _columnsTeamTable.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTable.put("groupId", new TableInfo.Column("groupId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTable.put("groupAvatarUrl", new TableInfo.Column("groupAvatarUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTable.put("groupName", new TableInfo.Column("groupName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTable.put("groupNick", new TableInfo.Column("groupNick", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTable.put("createTime", new TableInfo.Column("createTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTeamTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTeamTable = new HashSet<TableInfo.Index>(1);
        _indicesTeamTable.add(new TableInfo.Index("index_team_table_groupId", true, Arrays.asList("groupId")));
        final TableInfo _infoTeamTable = new TableInfo("team_table", _columnsTeamTable, _foreignKeysTeamTable, _indicesTeamTable);
        final TableInfo _existingTeamTable = TableInfo.read(_db, "team_table");
        if (! _infoTeamTable.equals(_existingTeamTable)) {
          return new RoomOpenHelper.ValidationResult(false, "team_table(com.dq.im.model.TeamModel).\n"
                  + " Expected:\n" + _infoTeamTable + "\n"
                  + " Found:\n" + _existingTeamTable);
        }
        final HashMap<String, TableInfo.Column> _columnsHomeMessage = new HashMap<String, TableInfo.Column>(12);
        _columnsHomeMessage.put("msgIdClient", new TableInfo.Column("msgIdClient", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeMessage.put("msgIdServer", new TableInfo.Column("msgIdServer", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeMessage.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeMessage.put("msgType", new TableInfo.Column("msgType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeMessage.put("msgSecondType", new TableInfo.Column("msgSecondType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeMessage.put("createTime", new TableInfo.Column("createTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeMessage.put("sourceContent", new TableInfo.Column("sourceContent", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeMessage.put("messageSendStatus", new TableInfo.Column("messageSendStatus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeMessage.put("toUserId", new TableInfo.Column("toUserId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeMessage.put("fromUserId", new TableInfo.Column("fromUserId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeMessage.put("unReadNumber", new TableInfo.Column("unReadNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeMessage.put("groupId", new TableInfo.Column("groupId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHomeMessage = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHomeMessage = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHomeMessage = new TableInfo("home_message", _columnsHomeMessage, _foreignKeysHomeMessage, _indicesHomeMessage);
        final TableInfo _existingHomeMessage = TableInfo.read(_db, "home_message");
        if (! _infoHomeMessage.equals(_existingHomeMessage)) {
          return new RoomOpenHelper.ValidationResult(false, "home_message(com.dq.im.model.HomeImBaseMode).\n"
                  + " Expected:\n" + _infoHomeMessage + "\n"
                  + " Found:\n" + _existingHomeMessage);
        }
        final HashMap<String, TableInfo.Column> _columnsTeamUserJoin = new HashMap<String, TableInfo.Column>(2);
        _columnsTeamUserJoin.put("teamId", new TableInfo.Column("teamId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamUserJoin.put("userId", new TableInfo.Column("userId", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTeamUserJoin = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysTeamUserJoin.add(new TableInfo.ForeignKey("team_table", "NO ACTION", "NO ACTION",Arrays.asList("teamId"), Arrays.asList("groupId")));
        _foreignKeysTeamUserJoin.add(new TableInfo.ForeignKey("user_table", "NO ACTION", "NO ACTION",Arrays.asList("userId"), Arrays.asList("userId")));
        final HashSet<TableInfo.Index> _indicesTeamUserJoin = new HashSet<TableInfo.Index>(2);
        _indicesTeamUserJoin.add(new TableInfo.Index("index_team_user_join_teamId", false, Arrays.asList("teamId")));
        _indicesTeamUserJoin.add(new TableInfo.Index("index_team_user_join_userId", false, Arrays.asList("userId")));
        final TableInfo _infoTeamUserJoin = new TableInfo("team_user_join", _columnsTeamUserJoin, _foreignKeysTeamUserJoin, _indicesTeamUserJoin);
        final TableInfo _existingTeamUserJoin = TableInfo.read(_db, "team_user_join");
        if (! _infoTeamUserJoin.equals(_existingTeamUserJoin)) {
          return new RoomOpenHelper.ValidationResult(false, "team_user_join(com.dq.im.model.TeamUserJoinModel).\n"
                  + " Expected:\n" + _infoTeamUserJoin + "\n"
                  + " Found:\n" + _existingTeamUserJoin);
        }
        final HashMap<String, TableInfo.Column> _columnsTeamMessage = new HashMap<String, TableInfo.Column>(11);
        _columnsTeamMessage.put("msgIdClient", new TableInfo.Column("msgIdClient", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamMessage.put("msgIdServer", new TableInfo.Column("msgIdServer", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamMessage.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamMessage.put("msgType", new TableInfo.Column("msgType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamMessage.put("msgSecondType", new TableInfo.Column("msgSecondType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamMessage.put("createTime", new TableInfo.Column("createTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamMessage.put("sourceContent", new TableInfo.Column("sourceContent", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamMessage.put("messageSendStatus", new TableInfo.Column("messageSendStatus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamMessage.put("toUserId", new TableInfo.Column("toUserId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamMessage.put("fromUserId", new TableInfo.Column("fromUserId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamMessage.put("groupId", new TableInfo.Column("groupId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTeamMessage = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTeamMessage = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTeamMessage = new TableInfo("team_message", _columnsTeamMessage, _foreignKeysTeamMessage, _indicesTeamMessage);
        final TableInfo _existingTeamMessage = TableInfo.read(_db, "team_message");
        if (! _infoTeamMessage.equals(_existingTeamMessage)) {
          return new RoomOpenHelper.ValidationResult(false, "team_message(com.dq.im.model.TeamMessageBaseModel).\n"
                  + " Expected:\n" + _infoTeamMessage + "\n"
                  + " Found:\n" + _existingTeamMessage);
        }
        final HashMap<String, TableInfo.Column> _columnsPersonMessage = new HashMap<String, TableInfo.Column>(10);
        _columnsPersonMessage.put("msgIdClient", new TableInfo.Column("msgIdClient", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonMessage.put("msgIdServer", new TableInfo.Column("msgIdServer", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonMessage.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonMessage.put("msgType", new TableInfo.Column("msgType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonMessage.put("msgSecondType", new TableInfo.Column("msgSecondType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonMessage.put("createTime", new TableInfo.Column("createTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonMessage.put("sourceContent", new TableInfo.Column("sourceContent", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonMessage.put("messageSendStatus", new TableInfo.Column("messageSendStatus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonMessage.put("toUserId", new TableInfo.Column("toUserId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPersonMessage.put("fromUserId", new TableInfo.Column("fromUserId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPersonMessage = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPersonMessage = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPersonMessage = new TableInfo("person_message", _columnsPersonMessage, _foreignKeysPersonMessage, _indicesPersonMessage);
        final TableInfo _existingPersonMessage = TableInfo.read(_db, "person_message");
        if (! _infoPersonMessage.equals(_existingPersonMessage)) {
          return new RoomOpenHelper.ValidationResult(false, "person_message(com.dq.im.model.P2PMessageBaseModel).\n"
                  + " Expected:\n" + _infoPersonMessage + "\n"
                  + " Found:\n" + _existingPersonMessage);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "1103de7e3ebedf31aeca600db0d132f5", "25426fed289af98a820d9fe53b95be71");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "user_table","team_table","home_message","team_user_join","team_message","person_message");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `user_table`");
      _db.execSQL("DELETE FROM `team_table`");
      _db.execSQL("DELETE FROM `home_message`");
      _db.execSQL("DELETE FROM `team_user_join`");
      _db.execSQL("DELETE FROM `team_message`");
      _db.execSQL("DELETE FROM `person_message`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public TeamDao teamDao() {
    if (_teamDao != null) {
      return _teamDao;
    } else {
      synchronized(this) {
        if(_teamDao == null) {
          _teamDao = new TeamDao_Impl(this);
        }
        return _teamDao;
      }
    }
  }

  @Override
  public P2PMessageBaseDao p2pMessageBaseDao() {
    if (_p2PMessageBaseDao != null) {
      return _p2PMessageBaseDao;
    } else {
      synchronized(this) {
        if(_p2PMessageBaseDao == null) {
          _p2PMessageBaseDao = new P2PMessageBaseDao_Impl(this);
        }
        return _p2PMessageBaseDao;
      }
    }
  }

  @Override
  public TeamMessageBaseDao teamMessageBaseDao() {
    if (_teamMessageBaseDao != null) {
      return _teamMessageBaseDao;
    } else {
      synchronized(this) {
        if(_teamMessageBaseDao == null) {
          _teamMessageBaseDao = new TeamMessageBaseDao_Impl(this);
        }
        return _teamMessageBaseDao;
      }
    }
  }

  @Override
  public HomeImBaseDao homeImBaseDao() {
    if (_homeImBaseDao != null) {
      return _homeImBaseDao;
    } else {
      synchronized(this) {
        if(_homeImBaseDao == null) {
          _homeImBaseDao = new HomeImBaseDao_Impl(this);
        }
        return _homeImBaseDao;
      }
    }
  }

  @Override
  public TeamUserJoinDao teamUserJoinDao() {
    if (_teamUserJoinDao != null) {
      return _teamUserJoinDao;
    } else {
      synchronized(this) {
        if(_teamUserJoinDao == null) {
          _teamUserJoinDao = new TeamUserJoinDao_Impl(this);
        }
        return _teamUserJoinDao;
      }
    }
  }
}
