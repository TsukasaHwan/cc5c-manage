package com.cc5c.task;

import com.cc5c.entity.pojo.Device;
import com.cc5c.entity.pojo.DeviceHistory;
import com.cc5c.entity.pojo.User;
import com.cc5c.mapper.DeviceHistoryMapper;
import com.cc5c.mapper.DeviceMapper;
import com.cc5c.mapper.UserMapper;
import com.cc5c.utils.MailUtil;
import com.cc5c.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SchedulerTask {
    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceHistoryMapper deviceHistoryMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailUtil mailUtil;

    //272185948@qq.com
    @Scheduled(cron = "0 0 0 * * ?")
    public void notice() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Device> devices = deviceMapper.selectList(null);
        List<User> userAll = userMapper.selectList(null);
        String[] tos = userAll.stream().map(User::getEmail).filter(StringUtils::isNotBlank).toArray(String[]::new);
        DeviceHistory deviceHistory = null;
        for (Device device : devices) {
            Date date1 = device.getCheckTime();
            Date date2 = device.getBlastFindTime();
            Date date3 = device.getChangeTime();
            String checkTime = "";
            String blastFindTime = "";
            String changeTime = "";
            if (date1 != null) {
                checkTime = sdf.format(date1);
            }
            if (date2 != null) {
                blastFindTime = sdf.format(date2);
            }
            if (date3 != null) {
                changeTime = sdf.format(date3);
            }
            String currentDate = sdf.format(new Date());
            if (checkTime.equals(currentDate) || blastFindTime.equals(currentDate) || changeTime.equals(currentDate)) {
                StringBuffer sb = new StringBuffer(device.getDeviceName() + "的");
                if (checkTime.equals(currentDate)) {
                    sb.append("设备检修时间、");
                }
                if (blastFindTime.equals(currentDate)) {
                    sb.append("防爆检查时间、");
                }
                if (changeTime.equals(currentDate)) {
                    sb.append("防爆合格证丶完好牌丶整定牌更换日期");
                }
                String s = sb.toString();
                if (s.endsWith("、")) {
                    s = s.substring(0, sb.lastIndexOf("、"));
                    sb = new StringBuffer(s);
                }
                sb.append("到了已经入待办请注意修改");
//                    MailUtil.sendMessage("待办提醒", NOTICE_ACCOUNT, sb.toString());
                try {
                    for (String to : tos) {
                        mailUtil.sendMailWithText(sb.toString(), "待办提醒", to);
                    }
                } catch (Exception e) {
                    log.error("mail exception:", e);
                }
                deviceHistory = new DeviceHistory();
                deviceHistory.setDeviceHistoryId(UUIDUtil.getUUID());
                deviceHistory.setDeviceId(device.getDeviceId());
                deviceHistory.setCategoryId(device.getCategoryId());
                deviceHistory.setDeviceName(device.getDeviceName());
                deviceHistory.setDeviceType(device.getDeviceType());
                deviceHistory.setDeviceNo(device.getDeviceNo());
                deviceHistory.setCoalSafetyNo(device.getCoalSafetyNo());
                deviceHistory.setProducer(device.getProducer());
                deviceHistory.setUsePlace(device.getUsePlace());
                deviceHistory.setIntoWellTime(device.getIntoWellTime());
                deviceHistory.setAcceptorResult(device.getAcceptorResult());
                deviceHistory.setInstallResult(device.getInstallResult());
                deviceHistory.setInstallTime(device.getInstallTime());
                deviceHistory.setDesiccantReplaceTime(device.getDesiccantReplaceTime());
                deviceHistory.setCheckTime(device.getCheckTime());
                deviceHistory.setDeviceStatus(device.getDeviceStatus());
                deviceHistory.setDeviceUpTime(device.getDeviceUpTime());
                deviceHistory.setRemark(device.getRemark());
                deviceHistory.setIsUpcoming(Short.valueOf("0"));
                deviceHistory.setInsertTime(new Date());
                deviceHistory.setChangeTime(device.getChangeTime());
                deviceHistory.setCheckResult(device.getCheckResult());
                deviceHistory.setChangeResult(device.getChangeResult());

                //新增字段
                deviceHistory.setUseCompany(device.getUseCompany());
                deviceHistory.setUsers(device.getUsers());
                deviceHistory.setDeviceInfo(device.getDeviceInfo());
                deviceHistory.setBlastFindTime(device.getBlastFindTime());
                deviceHistory.setBlastFindPerson(device.getBlastFindPerson());
                deviceHistory.setBlastFindInfo(device.getBlastFindInfo());
                deviceHistory.setBlastInfoSeePerson(device.getBlastInfoSeePerson());
                deviceHistory.setDeviceTestTime(device.getDeviceTestTime());
                deviceHistory.setTestPerson(device.getTestPerson());
                deviceHistory.setCheckInfo(device.getCheckInfo());
                deviceHistory.setDeviceRecord(device.getDeviceRecord());
                deviceHistoryMapper.insert(deviceHistory);
            }
        }
    }
}
