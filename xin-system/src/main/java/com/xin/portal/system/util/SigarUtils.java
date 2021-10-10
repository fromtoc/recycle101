package com.xin.portal.system.util;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.util.ResourceUtils;

import com.xin.portal.system.model.ServerComputer;

public class SigarUtils {

	public final static Sigar sigar = initSigar();
	
	private static Sigar initSigar() {
		try {
			// 此处只为得到依赖库文件的目录，可根据实际项目自定义
			File classPath = new File(ResourceUtils.getURL("classpath:").getPath() + "sigar");
			String path = System.getProperty("java.library.path");
			String sigarLibPath = classPath.getCanonicalPath();
			// 为防止java.library.path重复加，此处判断了一下
			if (!path.contains(sigarLibPath)) {
				if (isOSWin()) {
					path += ";" + sigarLibPath;
				} else {
					path += ":" + sigarLibPath;
				}
				System.setProperty("java.library.path", path);
			}
			return new Sigar();
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isOSWin() {// OS 版本判断
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0) {
			return true;
		} else
			return false;
	}
	
	public static ServerComputer getSysInfo() {
		ServerComputer serverComputer = new ServerComputer();
		try { 
            // System信息，从jvm获取 
			property(serverComputer); 
            // cpu信息 
            cpu(serverComputer); 
            // 内存信息 
            memory(serverComputer); 
            /*// 操作系统信息 
            SigarUtils.os(); 
            // 用户信息 
            SigarUtils.who(); 
            // 文件系统信息 
            SigarUtils.file(); 
            // 网络信息 
            SigarUtils.net(); 
            // 以太网信息 
            SigarUtils.ethernet(); */
        } catch (Exception e1) { 
            e1.printStackTrace(); 
        }
		return serverComputer;
	}

	// System信息，从jvm获取
	public static void property(ServerComputer serverComputer) throws UnknownHostException {
		Runtime r = Runtime.getRuntime();
		Properties props = System.getProperties();
		InetAddress addr;
		addr = InetAddress.getLocalHost();
		//String ip = addr.getHostAddress();
		Map<String, String> map = System.getenv();
		//String userName = map.get("USERNAME");// 获取用户名
		serverComputer.setName(map.get("COMPUTERNAME"));// 获取计算机名
		serverComputer.setIp(addr.getHostAddress());// 获取计算机名ip
		serverComputer.setDomain(map.get("USERDOMAIN"));// 获取计算机域名
		serverComputer.setHostName(addr.getHostName());// 获取本地主机名
		serverComputer.setOsName(props.getProperty("os.name"));//操作系统的名称
		serverComputer.setOsArch(props.getProperty("os.arch"));//操作系统的构架
		serverComputer.setOsVersion(props.getProperty("os.version"));//操作系统的版本
		//String computerName = map.get("COMPUTERNAME");
		//String userDomain = map.get("USERDOMAIN");// 获取计算机域名
		//System.out.println("用户名:    " + userName);
		//System.out.println("计算机名:    " + computerName);
		//System.out.println("计算机域名:    " + userDomain);
		//System.out.println("本地ip地址:    " + ip);
		//System.out.println("本地主机名:    " + addr.getHostName());
		serverComputer.setJavaVersion(props.getProperty("java.version"));
		serverComputer.setJvmName(props.getProperty("java.vm.name"));
		serverComputer.setJvmTotal(r.totalMemory()/1024L/1024L);
		serverComputer.setJvmFree(r.freeMemory()/ 1024L/1024L);
		serverComputer.setJvmUsed((r.totalMemory()-r.freeMemory())/1024L/1024L);
		double jvmUsed = (double) Math.round(r.totalMemory()-r.freeMemory());
		serverComputer.setJvmUsedRate(String.format("%.2f", jvmUsed/r.totalMemory()*100)+"%");
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		serverComputer.setJvmStartTime(new Date(bean.getStartTime()));
		Long runTime = (new Date().getTime()-bean.getStartTime())/1000;
		serverComputer.setJvmRunTime(secondToTime(runTime));
		//System.out.println("Start Time = " + bean.getStartTime());
        //System.out.println("Start Date = " + new Date(bean.getStartTime()));
		/*System.out.println("JVM可以使用的总内存:    " + r.totalMemory());
		System.out.println("JVM可以使用的剩余内存:    " + r.freeMemory());
		System.out.println("JVM可以使用的处理器个数:    " + r.availableProcessors());
		System.out.println("Java的运行环境版本：    " + props.getProperty("java.version"));
		System.out.println("Java的运行环境供应商：    " + props.getProperty("java.vendor"));
		System.out.println("Java供应商的URL：    " + props.getProperty("java.vendor.url"));
		System.out.println("Java的安装路径：    " + props.getProperty("java.home"));
		System.out.println("Java的虚拟机规范版本：    " + props.getProperty("java.vm.specification.version"));
		System.out.println("Java的虚拟机规范供应商：    " + props.getProperty("java.vm.specification.vendor"));
		System.out.println("Java的虚拟机规范名称：    " + props.getProperty("java.vm.specification.name"));
		System.out.println("Java的虚拟机实现版本：    " + props.getProperty("java.vm.version"));
		System.out.println("Java的虚拟机实现供应商：    " + props.getProperty("java.vm.vendor"));
		System.out.println("Java的虚拟机实现名称：    " + props.getProperty("java.vm.name"));
		System.out.println("Java运行时环境规范版本：    " + props.getProperty("java.specification.version"));
		System.out.println("Java运行时环境规范供应商：    " + props.getProperty("java.specification.vender"));
		System.out.println("Java运行时环境规范名称：    " + props.getProperty("java.specification.name"));
		System.out.println("Java的类格式版本号：    " + props.getProperty("java.class.version"));
		System.out.println("Java的类路径：    " + props.getProperty("java.class.path"));
		System.out.println("加载库时搜索的路径列表：    " + props.getProperty("java.library.path"));
		System.out.println("默认的临时文件路径：    " + props.getProperty("java.io.tmpdir"));
		System.out.println("一个或多个扩展目录的路径：    " + props.getProperty("java.ext.dirs"));
		//System.out.println("操作系统的名称：    " + props.getProperty("os.name"));
		//System.out.println("操作系统的构架：    " + props.getProperty("os.arch"));
		//System.out.println("操作系统的版本：    " + props.getProperty("os.version"));
		System.out.println("文件分隔符：    " + props.getProperty("file.separator"));
		System.out.println("路径分隔符：    " + props.getProperty("path.separator"));
		System.out.println("行分隔符：    " + props.getProperty("line.separator"));
		System.out.println("用户的账户名称：    " + props.getProperty("user.name"));
		System.out.println("用户的主目录：    " + props.getProperty("user.home"));
		System.out.println("用户的当前工作目录：    " + props.getProperty("user.dir"));*/
	}

	public static void memory(ServerComputer serverComputer) throws SigarException {
		Mem mem = sigar.getMem();
		// 内存总量
		serverComputer.setMemoryTotal(mem.getTotal()/1024L/1024L);
		//System.out.println("内存总量:    " + mem.getTotal() / 1024L + "K av");
		// 当前内存使用量
		serverComputer.setMemoryUsed(mem.getUsed()/1024L/1024L);
		//System.out.println("当前内存使用量:    " + mem.getUsed() / 1024L + "K");
		// 当前内存剩余量
		serverComputer.setMemoryFree(mem.getFree()/1024L/1024L);
		double memoryUsed = mem.getUsed()/1024L;
		double memoryTotal = mem.getTotal()/1024L;
		serverComputer.setMemoryUsedRate(String.format("%.2f", memoryUsed/memoryTotal*100) +"%");
		//System.out.println("当前内存剩余量:    " + mem.getFree() / 1024L + "K");
		/*Swap swap = sigar.getSwap();
		// 交换区总量
		System.out.println("交换区总量:    " + swap.getTotal() / 1024L + "K av");
		// 当前交换区使用量
		System.out.println("当前交换区使用量:    " + swap.getUsed() / 1024L + "K used");
		// 当前交换区剩余量
		System.out.println("当前交换区剩余量:    " + swap.getFree() / 1024L + "K free");*/
	}

	public static void cpu(ServerComputer serverComputer) throws SigarException {
		CpuInfo infos[] = sigar.getCpuInfoList();
		CpuPerc cpuList[] = null;
		cpuList = sigar.getCpuPercList();
		serverComputer.setCpuNum(infos.length);
		for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
			CpuInfo info = infos[i];
			serverComputer.setCpuTotal(serverComputer.getCpuTotal()+info.getMhz());
			//System.out.println("第" + (i + 1) + "块CPU信息");
			//System.out.println("CPU的总量MHz:    " + info.getMhz());// CPU的总量MHz
			//System.out.println("CPU生产商:    " + info.getVendor());// 获得CPU的卖主，如：Intel
			//System.out.println("CPU类别:    " + info.getModel());// 获得CPU的类别，如：Celeron
			//System.out.println("CPU缓存数量:    " + info.getCacheSize());// 缓冲存储器数量
			printCpuPerc(cpuList[i],serverComputer);
		}
		serverComputer.setCpuUsedRate(serverComputer.getCpuUsedRate()/infos.length*100);
		serverComputer.setCpuSysRate(serverComputer.getCpuSysRate()/infos.length*100);
		serverComputer.setCpuIdleRate(serverComputer.getCpuIdleRate()/infos.length*100);
	}

	private static void printCpuPerc(CpuPerc cpu,ServerComputer serverComputer) {
		serverComputer.setCpuUsedRate(serverComputer.getCpuUsedRate()+cpu.getUser());
		serverComputer.setCpuSysRate(serverComputer.getCpuSysRate()+cpu.getSys());
		serverComputer.setCpuIdleRate(serverComputer.getCpuIdleRate()+cpu.getIdle());
		
		//System.out.println("CPU用户使用率:    " + CpuPerc.format(cpu.getUser()));// 用户使用率
		//System.out.println("CPU系统使用率:    " + CpuPerc.format(cpu.getSys()));// 系统使用率
		//System.out.println("CPU当前等待率:    " + CpuPerc.format(cpu.getWait()));// 当前等待率
		//System.out.println("CPU当前错误率:    " + CpuPerc.format(cpu.getNice()));//
		//System.out.println("CPU当前空闲率:    " + CpuPerc.format(cpu.getIdle()));// 当前空闲率
		//System.out.println("CPU总的使用率:    " + CpuPerc.format(cpu.getCombined()));// 总的使用率
	}

	public static void os() {
		/*OperatingSystem OS = OperatingSystem.getInstance();
		// 操作系统内核类型如： 386、486、586等x86
		System.out.println("操作系统:    " + OS.getArch());
		System.out.println("操作系统CpuEndian():    " + OS.getCpuEndian());//
		System.out.println("操作系统DataModel():    " + OS.getDataModel());//
		// 系统描述
		System.out.println("操作系统的描述:    " + OS.getDescription());
		// 操作系统类型
		// System.out.println("OS.getName(): " + OS.getName());
		// System.out.println("OS.getPatchLevel(): " + OS.getPatchLevel());//
		// 操作系统的卖主
		System.out.println("操作系统的卖主:    " + OS.getVendor());
		// 卖主名称
		System.out.println("操作系统的卖主名:    " + OS.getVendorCodeName());
		// 操作系统名称
		System.out.println("操作系统名称:    " + OS.getVendorName());
		// 操作系统卖主类型
		System.out.println("操作系统卖主类型:    " + OS.getVendorVersion());
		// 操作系统的版本号
		System.out.println("操作系统的版本号:    " + OS.getVersion());*/
	}

	public static void who() throws SigarException {
		/*Who who[] = sigar.getWhoList();
		if (who != null && who.length > 0) {
			for (int i = 0; i < who.length; i++) {
				// System.out.println("当前系统进程表中的用户名" + String.valueOf(i));
				Who _who = who[i];
				System.out.println("用户控制台:    " + _who.getDevice());
				System.out.println("用户host:    " + _who.getHost());
				// System.out.println("getTime(): " + _who.getTime());
				// 当前系统进程表中的用户名
				System.out.println("当前系统进程表中的用户名:    " + _who.getUser());
			}
		}*/
	}

	public static void file() throws Exception {
		/*FileSystem fslist[] = sigar.getFileSystemList();
		for (int i = 0; i < fslist.length; i++) {
			System.out.println("分区的盘符名称" + i);
			FileSystem fs = fslist[i];
			// 分区的盘符名称
			System.out.println("盘符名称:    " + fs.getDevName());
			// 分区的盘符名称
			System.out.println("盘符路径:    " + fs.getDirName());
			System.out.println("盘符标志:    " + fs.getFlags());//
			// 文件系统类型，比如 FAT32、NTFS
			System.out.println("盘符类型:    " + fs.getSysTypeName());
			// 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
			System.out.println("盘符类型名:    " + fs.getTypeName());
			// 文件系统类型
			System.out.println("盘符文件系统类型:    " + fs.getType());
			FileSystemUsage usage = null;
			try {
				usage = sigar.getFileSystemUsage(fs.getDirName());
				switch (fs.getType()) {
				case 0: // TYPE_UNKNOWN ：未知
					break;
				case 1: // TYPE_NONE
					break;
				case 2: // TYPE_LOCAL_DISK : 本地硬盘
					// 文件系统总大小
					System.out.println(fs.getDevName() + "总大小:    " + usage.getTotal() + "KB");
					// 文件系统剩余大小
					System.out.println(fs.getDevName() + "剩余大小:    " + usage.getFree() + "KB");
					// 文件系统可用大小
					System.out.println(fs.getDevName() + "可用大小:    " + usage.getAvail() + "KB");
					// 文件系统已经使用量
					System.out.println(fs.getDevName() + "已经使用量:    " + usage.getUsed() + "KB");
					double usePercent = usage.getUsePercent() * 100D;
					// 文件系统资源的利用率
					System.out.println(fs.getDevName() + "资源的利用率:    " + usePercent + "%");
					break;
				case 3:// TYPE_NETWORK ：网络
					break;
				case 4:// TYPE_RAM_DISK ：闪存
					break;
				case 5:// TYPE_CDROM ：光驱
					break;
				case 6:// TYPE_SWAP ：页面交换
					break;
				}
				System.out.println(fs.getDevName() + "读出：    " + usage.getDiskReads());
				System.out.println(fs.getDevName() + "写入：    " + usage.getDiskWrites());
			} catch (Exception e) {
				System.out.println(e);
				System.out.println(fs.getDirName());
			}
		}
		return;*/
	}

	public static void net() throws Exception {
		/*String ifNames[] = sigar.getNetInterfaceList();
		for (int i = 0; i < ifNames.length; i++) {
			String name = ifNames[i];
			NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
			System.out.println("网络设备名:    " + name);// 网络设备名
			System.out.println("IP地址:    " + ifconfig.getAddress());// IP地址
			System.out.println("子网掩码:    " + ifconfig.getNetmask());// 子网掩码
			if ((ifconfig.getFlags() & 1L) <= 0L) {
				System.out.println("!IFF_UP...skipping getNetInterfaceStat");
				continue;
			}
			NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
			System.out.println(name + "接收的总包裹数:" + ifstat.getRxPackets());// 接收的总包裹数
			System.out.println(name + "发送的总包裹数:" + ifstat.getTxPackets());// 发送的总包裹数
			System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes());// 接收到的总字节数
			System.out.println(name + "发送的总字节数:" + ifstat.getTxBytes());// 发送的总字节数
			System.out.println(name + "接收到的错误包数:" + ifstat.getRxErrors());// 接收到的错误包数
			System.out.println(name + "发送数据包时的错误数:" + ifstat.getTxErrors());// 发送数据包时的错误数
			System.out.println(name + "接收时丢弃的包数:" + ifstat.getRxDropped());// 接收时丢弃的包数
			System.out.println(name + "发送时丢弃的包数:" + ifstat.getTxDropped());// 发送时丢弃的包数
		}*/
	}

	public static void ethernet() throws SigarException {
		/*String[] ifaces = sigar.getNetInterfaceList();
		for (int i = 0; i < ifaces.length; i++) {
			NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
			if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
					|| NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
				continue;
			}
			System.out.println(cfg.getName() + "IP地址:" + cfg.getAddress());// IP地址
			System.out.println(cfg.getName() + "网关广播地址:" + cfg.getBroadcast());// 网关广播地址
			System.out.println(cfg.getName() + "网卡MAC地址:" + cfg.getHwaddr());// 网卡MAC地址
			System.out.println(cfg.getName() + "子网掩码:" + cfg.getNetmask());// 子网掩码
			System.out.println(cfg.getName() + "网卡描述信息:" + cfg.getDescription());// 网卡描述信息
			System.out.println(cfg.getName() + "网卡类型" + cfg.getType());//
		}*/
	}
	
	/**
     * 返回日时分秒
     * @param second
     * @return
     */
    private static String secondToTime(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (0 < days){
            return days + "天"+hours+"小时"+minutes+"分"+second+"秒";
        }else {
            return hours+"小时"+minutes+"分"+second+"秒";
        }
    }

}
