package com.dq.im.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.dq.im.model.ChatTeamModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.model.UserModel;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TeamMessageBaseDao_Impl implements TeamMessageBaseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TeamMessageBaseModel> __insertionAdapterOfTeamMessageBaseModel;

  private final EntityDeletionOrUpdateAdapter<TeamMessageBaseModel> __updateAdapterOfTeamMessageBaseModel;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMessageSendStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTeamPMessageByClientId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMessageForGroupId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMessageForClientId;

  public TeamMessageBaseDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTeamMessageBaseModel = new EntityInsertionAdapter<TeamMessageBaseModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `team_message` (`msgIdClient`,`msgIdServer`,`type`,`msgType`,`msgSecondType`,`createTime`,`sourceContent`,`messageSendStatus`,`toUserId`,`fromUserId`,`groupId`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TeamMessageBaseModel value) {
        if (value.getMsgIdClient() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMsgIdClient());
        }
        if (value.getMsgIdServer() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getMsgIdServer());
        }
        if (value.getType() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getType());
        }
        if (value.getMsgType() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getMsgType());
        }
        if (value.getMsgSecondType() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getMsgSecondType());
        }
        stmt.bindLong(6, value.getCreateTime());
        if (value.getSourceContent() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getSourceContent());
        }
        stmt.bindLong(8, value.getMessageSendStatus());
        if (value.getToUserId() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getToUserId());
        }
        if (value.getFromUserId() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getFromUserId());
        }
        if (value.getGroupId() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getGroupId());
        }
      }
    };
    this.__updateAdapterOfTeamMessageBaseModel = new EntityDeletionOrUpdateAdapter<TeamMessageBaseModel>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `team_message` SET `msgIdClient` = ?,`msgIdServer` = ?,`type` = ?,`msgType` = ?,`msgSecondType` = ?,`createTime` = ?,`sourceContent` = ?,`messageSendStatus` = ?,`toUserId` = ?,`fromUserId` = ?,`groupId` = ? WHERE `msgIdClient` = ? AND `msgIdServer` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TeamMessageBaseModel value) {
        if (value.getMsgIdClient() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMsgIdClient());
        }
        if (value.getMsgIdServer() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getMsgIdServer());
        }
        if (value.getType() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getType());
        }
        if (value.getMsgType() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getMsgType());
        }
        if (value.getMsgSecondType() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getMsgSecondType());
        }
        stmt.bindLong(6, value.getCreateTime());
        if (value.getSourceContent() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getSourceContent());
        }
        stmt.bindLong(8, value.getMessageSendStatus());
        if (value.getToUserId() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getToUserId());
        }
        if (value.getFromUserId() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getFromUserId());
        }
        if (value.getGroupId() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getGroupId());
        }
        if (value.getMsgIdClient() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getMsgIdClient());
        }
        if (value.getMsgIdServer() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getMsgIdServer());
        }
      }
    };
    this.__preparedStmtOfUpdateMessageSendStatus = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE team_message SET sourceContent = ? , messageSendStatus = ? , msgIdServer = ? WHERE msgIdClient = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTeamPMessageByClientId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE team_message SET msgIdServer = ? ,messageSendStatus = ?,sourceContent = ? WHERE msgIdClient = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM team_message";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMessageForGroupId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM team_message WHERE groupId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMessageForClientId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM team_message WHERE msgIdClient = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final TeamMessageBaseModel teamMessageBaseModel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTeamMessageBaseModel.insert(teamMessageBaseModel);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final List<TeamMessageBaseModel> teamMessageBaseModels) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTeamMessageBaseModel.insert(teamMessageBaseModels);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final TeamMessageBaseModel teamMessageBaseModel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTeamMessageBaseModel.handle(teamMessageBaseModel);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateMessageSendStatus(final String sourceContent, final int messageSendStatus,
      final String msgServerId, final String msgClientId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMessageSendStatus.acquire();
    int _argIndex = 1;
    if (sourceContent == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, sourceContent);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, messageSendStatus);
    _argIndex = 3;
    if (msgServerId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, msgServerId);
    }
    _argIndex = 4;
    if (msgClientId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, msgClientId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateMessageSendStatus.release(_stmt);
    }
  }

  @Override
  public void updateTeamPMessageByClientId(final String clientId, final String serverId,
      final int messageSendStatus, final String sourceContent) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTeamPMessageByClientId.acquire();
    int _argIndex = 1;
    if (serverId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, serverId);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, messageSendStatus);
    _argIndex = 3;
    if (sourceContent == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, sourceContent);
    }
    _argIndex = 4;
    if (clientId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, clientId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTeamPMessageByClientId.release(_stmt);
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public void deleteMessageForGroupId(final String groupId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessageForGroupId.acquire();
    int _argIndex = 1;
    if (groupId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, groupId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteMessageForGroupId.release(_stmt);
    }
  }

  @Override
  public void deleteMessageForClientId(final String msgClientId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessageForClientId.acquire();
    int _argIndex = 1;
    if (msgClientId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, msgClientId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteMessageForClientId.release(_stmt);
    }
  }

  @Override
  public LiveData<List<TeamMessageBaseModel>> getAllTeamMessage() {
    final String _sql = "SELECT * FROM team_message ORDER BY createTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"team_message"}, false, new Callable<List<TeamMessageBaseModel>>() {
      @Override
      public List<TeamMessageBaseModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMsgIdClient = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdClient");
          final int _cursorIndexOfMsgIdServer = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdServer");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfMsgType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgType");
          final int _cursorIndexOfMsgSecondType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgSecondType");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfSourceContent = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceContent");
          final int _cursorIndexOfMessageSendStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "messageSendStatus");
          final int _cursorIndexOfToUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "toUserId");
          final int _cursorIndexOfFromUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "fromUserId");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final List<TeamMessageBaseModel> _result = new ArrayList<TeamMessageBaseModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TeamMessageBaseModel _item;
            _item = new TeamMessageBaseModel();
            final String _tmpMsgIdClient;
            _tmpMsgIdClient = _cursor.getString(_cursorIndexOfMsgIdClient);
            _item.setMsgIdClient(_tmpMsgIdClient);
            final String _tmpMsgIdServer;
            _tmpMsgIdServer = _cursor.getString(_cursorIndexOfMsgIdServer);
            _item.setMsgIdServer(_tmpMsgIdServer);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            _item.setType(_tmpType);
            final String _tmpMsgType;
            _tmpMsgType = _cursor.getString(_cursorIndexOfMsgType);
            _item.setMsgType(_tmpMsgType);
            final String _tmpMsgSecondType;
            _tmpMsgSecondType = _cursor.getString(_cursorIndexOfMsgSecondType);
            _item.setMsgSecondType(_tmpMsgSecondType);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            _item.setCreateTime(_tmpCreateTime);
            final String _tmpSourceContent;
            _tmpSourceContent = _cursor.getString(_cursorIndexOfSourceContent);
            _item.setSourceContent(_tmpSourceContent);
            final int _tmpMessageSendStatus;
            _tmpMessageSendStatus = _cursor.getInt(_cursorIndexOfMessageSendStatus);
            _item.setMessageSendStatus(_tmpMessageSendStatus);
            final String _tmpToUserId;
            _tmpToUserId = _cursor.getString(_cursorIndexOfToUserId);
            _item.setToUserId(_tmpToUserId);
            final String _tmpFromUserId;
            _tmpFromUserId = _cursor.getString(_cursorIndexOfFromUserId);
            _item.setFromUserId(_tmpFromUserId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            _item.setGroupId(_tmpGroupId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<TeamMessageBaseModel> getMessageByTeamId(final String teamId, final long createTime,
      final int limitCount) {
    final String _sql = "SELECT * FROM team_message WHERE team_message.groupId = ? AND team_message.createTime < ? ORDER BY createTime DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (teamId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teamId);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, createTime);
    _argIndex = 3;
    _statement.bindLong(_argIndex, limitCount);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMsgIdClient = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdClient");
      final int _cursorIndexOfMsgIdServer = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdServer");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfMsgType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgType");
      final int _cursorIndexOfMsgSecondType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgSecondType");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfSourceContent = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceContent");
      final int _cursorIndexOfMessageSendStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "messageSendStatus");
      final int _cursorIndexOfToUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "toUserId");
      final int _cursorIndexOfFromUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "fromUserId");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final List<TeamMessageBaseModel> _result = new ArrayList<TeamMessageBaseModel>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TeamMessageBaseModel _item;
        _item = new TeamMessageBaseModel();
        final String _tmpMsgIdClient;
        _tmpMsgIdClient = _cursor.getString(_cursorIndexOfMsgIdClient);
        _item.setMsgIdClient(_tmpMsgIdClient);
        final String _tmpMsgIdServer;
        _tmpMsgIdServer = _cursor.getString(_cursorIndexOfMsgIdServer);
        _item.setMsgIdServer(_tmpMsgIdServer);
        final String _tmpType;
        _tmpType = _cursor.getString(_cursorIndexOfType);
        _item.setType(_tmpType);
        final String _tmpMsgType;
        _tmpMsgType = _cursor.getString(_cursorIndexOfMsgType);
        _item.setMsgType(_tmpMsgType);
        final String _tmpMsgSecondType;
        _tmpMsgSecondType = _cursor.getString(_cursorIndexOfMsgSecondType);
        _item.setMsgSecondType(_tmpMsgSecondType);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _item.setCreateTime(_tmpCreateTime);
        final String _tmpSourceContent;
        _tmpSourceContent = _cursor.getString(_cursorIndexOfSourceContent);
        _item.setSourceContent(_tmpSourceContent);
        final int _tmpMessageSendStatus;
        _tmpMessageSendStatus = _cursor.getInt(_cursorIndexOfMessageSendStatus);
        _item.setMessageSendStatus(_tmpMessageSendStatus);
        final String _tmpToUserId;
        _tmpToUserId = _cursor.getString(_cursorIndexOfToUserId);
        _item.setToUserId(_tmpToUserId);
        final String _tmpFromUserId;
        _tmpFromUserId = _cursor.getString(_cursorIndexOfFromUserId);
        _item.setFromUserId(_tmpFromUserId);
        final String _tmpGroupId;
        _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
        _item.setGroupId(_tmpGroupId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<ChatTeamModel>> getMessageAndUserByTeamId(final String teamId) {
    final String _sql = "SELECT * FROM team_message,user_table WHERE team_message.fromUserId = user_table.userId AND team_message.groupId = ? ORDER BY createTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (teamId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teamId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"team_message","user_table"}, false, new Callable<List<ChatTeamModel>>() {
      @Override
      public List<ChatTeamModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMsgIdClient = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdClient");
          final int _cursorIndexOfMsgIdServer = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdServer");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfMsgType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgType");
          final int _cursorIndexOfMsgSecondType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgSecondType");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfSourceContent = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceContent");
          final int _cursorIndexOfMessageSendStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "messageSendStatus");
          final int _cursorIndexOfToUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "toUserId");
          final int _cursorIndexOfFromUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "fromUserId");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfNick = CursorUtil.getColumnIndexOrThrow(_cursor, "nick");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfPicUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "picUrl");
          final int _cursorIndexOfIsFriend = CursorUtil.getColumnIndexOrThrow(_cursor, "isFriend");
          final List<ChatTeamModel> _result = new ArrayList<ChatTeamModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final ChatTeamModel _item;
            final TeamMessageBaseModel _tmpTeamMessageBaseModel;
            if (! (_cursor.isNull(_cursorIndexOfMsgIdClient) && _cursor.isNull(_cursorIndexOfMsgIdServer) && _cursor.isNull(_cursorIndexOfType) && _cursor.isNull(_cursorIndexOfMsgType) && _cursor.isNull(_cursorIndexOfMsgSecondType) && _cursor.isNull(_cursorIndexOfCreateTime) && _cursor.isNull(_cursorIndexOfSourceContent) && _cursor.isNull(_cursorIndexOfMessageSendStatus) && _cursor.isNull(_cursorIndexOfToUserId) && _cursor.isNull(_cursorIndexOfFromUserId) && _cursor.isNull(_cursorIndexOfGroupId))) {
              _tmpTeamMessageBaseModel = new TeamMessageBaseModel();
              final String _tmpMsgIdClient;
              _tmpMsgIdClient = _cursor.getString(_cursorIndexOfMsgIdClient);
              _tmpTeamMessageBaseModel.setMsgIdClient(_tmpMsgIdClient);
              final String _tmpMsgIdServer;
              _tmpMsgIdServer = _cursor.getString(_cursorIndexOfMsgIdServer);
              _tmpTeamMessageBaseModel.setMsgIdServer(_tmpMsgIdServer);
              final String _tmpType;
              _tmpType = _cursor.getString(_cursorIndexOfType);
              _tmpTeamMessageBaseModel.setType(_tmpType);
              final String _tmpMsgType;
              _tmpMsgType = _cursor.getString(_cursorIndexOfMsgType);
              _tmpTeamMessageBaseModel.setMsgType(_tmpMsgType);
              final String _tmpMsgSecondType;
              _tmpMsgSecondType = _cursor.getString(_cursorIndexOfMsgSecondType);
              _tmpTeamMessageBaseModel.setMsgSecondType(_tmpMsgSecondType);
              final long _tmpCreateTime;
              _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
              _tmpTeamMessageBaseModel.setCreateTime(_tmpCreateTime);
              final String _tmpSourceContent;
              _tmpSourceContent = _cursor.getString(_cursorIndexOfSourceContent);
              _tmpTeamMessageBaseModel.setSourceContent(_tmpSourceContent);
              final int _tmpMessageSendStatus;
              _tmpMessageSendStatus = _cursor.getInt(_cursorIndexOfMessageSendStatus);
              _tmpTeamMessageBaseModel.setMessageSendStatus(_tmpMessageSendStatus);
              final String _tmpToUserId;
              _tmpToUserId = _cursor.getString(_cursorIndexOfToUserId);
              _tmpTeamMessageBaseModel.setToUserId(_tmpToUserId);
              final String _tmpFromUserId;
              _tmpFromUserId = _cursor.getString(_cursorIndexOfFromUserId);
              _tmpTeamMessageBaseModel.setFromUserId(_tmpFromUserId);
              final String _tmpGroupId;
              _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
              _tmpTeamMessageBaseModel.setGroupId(_tmpGroupId);
            }  else  {
              _tmpTeamMessageBaseModel = null;
            }
            final UserModel _tmpUserModel;
            if (! (_cursor.isNull(_cursorIndexOfUserId) && _cursor.isNull(_cursorIndexOfUsername) && _cursor.isNull(_cursorIndexOfNick) && _cursor.isNull(_cursorIndexOfSex) && _cursor.isNull(_cursorIndexOfPicUrl) && _cursor.isNull(_cursorIndexOfIsFriend))) {
              _tmpUserModel = new UserModel();
              final String _tmpUserId;
              _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
              _tmpUserModel.setUserId(_tmpUserId);
              final String _tmpUsername;
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
              _tmpUserModel.setUsername(_tmpUsername);
              final String _tmpNick;
              _tmpNick = _cursor.getString(_cursorIndexOfNick);
              _tmpUserModel.setNick(_tmpNick);
              final String _tmpSex;
              _tmpSex = _cursor.getString(_cursorIndexOfSex);
              _tmpUserModel.setSex(_tmpSex);
              final String _tmpPicUrl;
              _tmpPicUrl = _cursor.getString(_cursorIndexOfPicUrl);
              _tmpUserModel.setPicUrl(_tmpPicUrl);
              final String _tmpIsFriend;
              _tmpIsFriend = _cursor.getString(_cursorIndexOfIsFriend);
              _tmpUserModel.setIsFriend(_tmpIsFriend);
            }  else  {
              _tmpUserModel = null;
            }
            _item = new ChatTeamModel();
            _item.setTeamMessageBaseModel(_tmpTeamMessageBaseModel);
            _item.setUserModel(_tmpUserModel);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<TeamMessageBaseModel> getTeamMessageByMessageId(final String msgServerId) {
    final String _sql = "SELECT * FROM team_message WHERE msgIdServer = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (msgServerId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, msgServerId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"team_message"}, false, new Callable<TeamMessageBaseModel>() {
      @Override
      public TeamMessageBaseModel call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMsgIdClient = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdClient");
          final int _cursorIndexOfMsgIdServer = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdServer");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfMsgType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgType");
          final int _cursorIndexOfMsgSecondType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgSecondType");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfSourceContent = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceContent");
          final int _cursorIndexOfMessageSendStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "messageSendStatus");
          final int _cursorIndexOfToUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "toUserId");
          final int _cursorIndexOfFromUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "fromUserId");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final TeamMessageBaseModel _result;
          if(_cursor.moveToFirst()) {
            _result = new TeamMessageBaseModel();
            final String _tmpMsgIdClient;
            _tmpMsgIdClient = _cursor.getString(_cursorIndexOfMsgIdClient);
            _result.setMsgIdClient(_tmpMsgIdClient);
            final String _tmpMsgIdServer;
            _tmpMsgIdServer = _cursor.getString(_cursorIndexOfMsgIdServer);
            _result.setMsgIdServer(_tmpMsgIdServer);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            _result.setType(_tmpType);
            final String _tmpMsgType;
            _tmpMsgType = _cursor.getString(_cursorIndexOfMsgType);
            _result.setMsgType(_tmpMsgType);
            final String _tmpMsgSecondType;
            _tmpMsgSecondType = _cursor.getString(_cursorIndexOfMsgSecondType);
            _result.setMsgSecondType(_tmpMsgSecondType);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            _result.setCreateTime(_tmpCreateTime);
            final String _tmpSourceContent;
            _tmpSourceContent = _cursor.getString(_cursorIndexOfSourceContent);
            _result.setSourceContent(_tmpSourceContent);
            final int _tmpMessageSendStatus;
            _tmpMessageSendStatus = _cursor.getInt(_cursorIndexOfMessageSendStatus);
            _result.setMessageSendStatus(_tmpMessageSendStatus);
            final String _tmpToUserId;
            _tmpToUserId = _cursor.getString(_cursorIndexOfToUserId);
            _result.setToUserId(_tmpToUserId);
            final String _tmpFromUserId;
            _tmpFromUserId = _cursor.getString(_cursorIndexOfFromUserId);
            _result.setFromUserId(_tmpFromUserId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            _result.setGroupId(_tmpGroupId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
