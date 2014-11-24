package com.github.dockerjava.api.model;

/**
 * The Linux capabilities supported by Docker.
 * The list of capabilities is defined in Docker's types.go,
 * {@link #ALL} was added manually.
 * 
 * @see <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">http://man7.org/linux/man-pages/man7/capabilities.7.html</a>
 */
public enum Capability {
	/**
	 * This meta capability includes all Linux capabilities.
	 */
	ALL,
	/**
	 * <ul>
	 *   <li>Enable and disable kernel auditing.
	 *   <li>Change auditing filter rules.
	 *   <li>Retrieve auditing status and filtering rules.
	 * </ul>
	 */
	AUDIT_CONTROL,
	/**
	 * Write records to kernel auditing log.
	 */
	AUDIT_WRITE,
	/**
	 * Employ features that can block system suspend.
	 */
	BLOCK_SUSPEND,
	/**
	 * Make arbitrary changes to file UIDs and GIDs (see chown(2)).
	 */
	CHOWN,
	/**
	 * Bypass file read, write, and execute permission checks.
	 * (DAC is an abbreviation of "discretionary access control".)
	 */
	DAC_OVERRIDE,
	/**
	 * Bypass file read permission checks and directory read and
	 * execute permission checks.
	 */
	DAC_READ_SEARCH,
	/**
	 * <ul>
	 *   <li>Bypass permission checks on operations that normally require
	 *     the file system UID of the process to match the UID of the file
	 *     (e.g., chmod(2), utime(2)), excluding those operations covered
	 *     by the {@link #DAC_OVERRIDE} and{@link #DAC_READ_SEARCH}.
	 *   <li>Set extended file attributes (see chattr(1)) on arbitrary files.
	 *   <li>Set Access Control Lists (ACLs) on arbitrary files.
	 *   <li>Ignore directory sticky bit on file deletion.
	 *   <li>Specify O_NOATIME for arbitrary files in open(2)and fcntl(2).
	 * </ul>
	 */
	FOWNER,
	/**
	 * <ul>
	 *   <li>Don't clear set-user-ID and set-group-ID permission bits when
	 *     a file is modified.
	 *   <li>Set the set-group-ID bit for a file whose GID does not match
	 *     the file system or any of the supplementary GIDs of the calling
	 *     process.
	 * </ul>
	 */
	FSETID,
	/**
	 * Permit memory locking (mlock(2), mlockall(2), mmap(2), shmctl(2)).
	 */
	IPC_LOCK,
	/**
	 * Bypass permission checks for operations on System V IPC objects.
	 */
	IPC_OWNER,
	/**
	 * Bypass permission checks for sending signals (see kill(2)).
	 * This includes use of the ioctl(2) KDSIGACCEPT operation.
	 */
	KILL,
	/**
	 * Establish leases on arbitrary files (see fcntl(2)).
	 */
	LEASE,
	/**
	 * Set the FS_APPEND_FL and FS_IMMUTABLE_FL i-node flags (see chattr(1)).
	 */
	LINUX_IMMUTABLE,
	/**
	 * Override Mandatory Access Control (MAC).
	 * Implemented for the Smack Linux Security Module (LSM).
	 */
	MAC_ADMIN,
	/**
	 * Allow MAC configuration or state changes. Implemented for the Smack LSM.
	 */
	MAC_OVERRIDE,
	/**
	 * Create special files using mknod(2).
	 */
	MKNOD,
	/**
	 * Perform various network-related operations:
	 * <ul>
	 *   <li>Interface configuration.
	 *   <li>Administration of IP firewall, masquerading, and accounting.
	 *   <li>Modify routing tables.
	 *   <li>Bind to any address for transparent proxying.
	 *   <li>Set type-of-service (TOS).
	 *   <li>Clear driver statistics.
	 *   <li>Set promiscuous mode.
	 *   <li>Enabling multicasting.
	 *   <li>Use setsockopt(2) to set the following socket options: SO_DEBUG,
	 *     SO_MARK, SO_PRIORITY (for a priority outside the range 0 to 6),
	 *     SO_RCVBUFFORCE, and SO_SNDBUFFORCE.
	 * </ul>
	 */
	NET_ADMIN,
	/**
	 * Bind a socket to Internet domain privileged ports (port numbers less
	 * than 1024).
	 */
	NET_BIND_SERVICE,
	/**
	 * (Unused) Make socket broadcasts, and listen to multicasts.
	 */
	NET_BROADCAST,
	/**
	 * <ul>
	 *   <li>Use RAW and PACKET sockets.
	 *   <li>Bind to any address for transparent proxying.
	 * </ul>
	 */
	NET_RAW,
	/**
	 * Set file capabilities.
	 */
	SETFCAP,
	/**
	 * <ul>
	 *   <li>Make arbitrary manipulations of process GIDs and supplementary
	 *     GID list.
	 *   <li>Forge GID when passing socket credentials via UNIX domain
	 *     sockets.
	 * </ul>
	 */
	SETGID,
	/**
	 * If file capabilities are not supported:
	 * <ul>
	 *   <li>grant or remove any capability in the caller's permitted
	 *     capability set to or from any other process. (This property of
	 *     CAP_SETPCAP is not available when the kernel is configured to
	 *     support file capabilities, since CAP_SETPCAP has entirely different
	 *     semantics for such kernels.)
	 * </ul>
	 * <p>
	 * If file capabilities are supported:
	 * <ul>
	 *   <li>Add any capability from the calling thread's bounding set to its
	 *     inheritable set.
	 *   <li>Drop capabilities from the bounding set (via prctl(2)
	 *     PR_CAPBSET_DROP).
	 *   <li>Make changes to the securebits flags.
	 * </ul>
	 */
	SETPCAP,
	/**
	 * <ul>
	 *   <li>Make arbitrary manipulations of process UIDs (setuid(2),
	 *     setreuid(2), setresuid(2), setfsuid(2)).
	 *   <li>Make forged UID when passing socket credentials via UNIX domain
	 *     sockets.
	 * </ul>
	 */
	SETUID,
	/**
	 * <ul>
	 *   <li>Perform a range of system administration operations including:
	 *     quotactl(2), mount(2), umount(2), swapon(2), swapoff(2), sethostname(2),
	 *     and setdomainname(2).
	 *   <li>Perform privileged syslog(2) operations (since Linux 2.6.37,
	 *     CAP_SYSLOG should be used to permit such operations).
	 *   <li>Perform VM86_REQUEST_IRQ vm86(2) command.
	 *   <li>Perform IPC_SET and IPC_RMID operations on arbitrary System V IPC objects.
	 *   <li>Perform operations on trusted and security Extended Attributes
	 *     (see attr(5)).
	 *   <li>Use lookup_dcookie(2)
	 *   <li>Use ioprio_set(2) to assign IOPRIO_CLASS_RT and (before Linux 2.6.25)
	 *     IOPRIO_CLASS_IDLE I/O scheduling classes.
	 *   <li>Forge UID when passing socket credentials.
	 *   <li>Exceed /proc/sys/fs/file-max, the system-wide limit on the number of
	 *     open files, in system calls that open files (e.g., accept(2), execve(2),
	 *     open(2), pipe(2)).
	 *   <li>Employ CLONE_* flags that create new namespaces with clone(2) and
	 *     unshare(2).
	 *   <li>Call perf_event_open(2).
	 *   <li>Access privileged perf event information.
	 *   <li>Call setns(2).
	 *   <li>Call fanotify_init(2).
	 *   <li>Perform KEYCTL_CHOWN and KEYCTL_SETPERM keyctl(2) operations.
	 *   <li>Perform madvise(2) MADV_HWPOISON operation.
	 *   <li>Employ the TIOCSTI ioctl(2) to insert characters into the input queue
	 *     of a terminal other than the caller's controlling terminal.
	 *   <li>Employ the obsolete nfsservctl(2) system call.
	 *   <li>Employ the obsolete bdflush(2) system call.
	 *   <li>Perform various privileged block-device ioctl(2) operations.
	 *   <li>Perform various privileged file-system ioctl(2) operations.
	 *   <li>Perform administrative operations on many device drivers.
	 * </ul>
	 */
	SYS_ADMIN,
	/**
	 * Use reboot(2) and kexec_load(2).
	 */
	SYS_BOOT,
	/**
	 * Use chroot(2).
	 */
	SYS_CHROOT,
	/**
	 * <ul>
	 *   <li>Perform privileged syslog(2) operations. See syslog(2) for information
	 *     on which operations require privilege.
	 *   <li>View kernel addresses exposed via /proc and other interfaces when
	 *     /proc/sys/kernel/kptr_restrict has the value 1. (See the discussion of the
	 *      kptr_restrict in proc(5).)
	 * </ul>
	 */
	SYSLOG,
	/**
	 * <ul>
	 *   <li>Load and unload kernel modules (see init_module(2) and delete_module(2))
	 *   <li>In kernels before 2.6.25: drop capabilities from the system-wide
	 *     capability bounding set.
	 * </ul>
	 */
	SYS_MODULE,
	/**
	 * <ul>
	 *   <li>Raise process nice value (nice(2), setpriority(2)) and change the nice
	 *     value for arbitrary processes.
	 *   <li>Set real-time scheduling policies for calling process, and set scheduling
	 *     policies and priorities for arbitrary processes (sched_setscheduler(2),
	 *     sched_setparam(2)).
	 *   <li>Set CPU affinity for arbitrary processes (sched_setaffinity(2)).
	 *   <li>Set I/O scheduling class and priority for arbitrary processes
	 *     (ioprio_set(2)).
	 *   <li>Apply migrate_pages(2) to arbitrary processes and allow processes to be
	 *     migrated to arbitrary nodes.
	 *   <li>Apply move_pages(2) to arbitrary processes.
	 *   <li>Use the MPOL_MF_MOVE_ALL flag with mbind(2) and move_pages(2).
	 * </ul>
	 */
	SYS_NICE,
	/**
	 * Use acct(2).
	 */
	SYS_PACCT,
	/**
	 * <ul>
	 *   <li>Trace arbitrary processes using ptrace(2).
	 *   <li>Apply get_robust_list(2) to arbitrary processes.
	 *   <li>Inspect processes using kcmp(2).
	 * </ul>
	 */
	SYS_PTRACE,
	/**
	 * <ul>
	 *   <li>Perform I/O port operations (iopl(2) and ioperm(2)).
	 *   <li>Access /proc/kcore.
	 *   <li>Employ the FIBMAP ioctl(2) operation.
	 *   <li>Open devices for accessing x86 model-specific registers (MSRs, see
	 *     msr(4)).
	 *   <li>Update /proc/sys/vm/mmap_min_addr.
	 *   <li>Create memory mappings at addresses below the value specified by
	 *     /proc/sys/vm/mmap_min_addr.
	 *   <li>Map files in /proc/pci/bus.
	 *   <li>Open /dev/mem and /dev/kmem.
	 *   <li>Perform various SCSI device commands.
	 *   <li>Perform certain operations on hpsa(4) and cciss(4) devices.
	 *   <li>Perform a range of device-specific operations on other devices.
	 * </ul>
	 */
	SYS_RAWIO,
	/**
	 * <ul>
	 *   <li>Use reserved space on ext2 file systems.
	 *   <li>Make ioctl(2) calls controlling ext3 journaling.
	 *   <li>Override disk quota limits.
	 *   <li>Increase resource limits (see setrlimit(2)).
	 *   <li>Override RLIMIT_NPROC resource limit.
	 *   <li>Override maximum number of consoles on console allocation.
	 *   <li>Override maximum number of keymaps.
	 *   <li>Allow more than 64hz interrupts from the real-time clock.
	 *   <li>Raise msg_qbytes limit for a System V message queue above the limit
	 *     in /proc/sys/kernel/msgmnb (see msgop(2) and msgctl(2)).
	 *   <li>Override the /proc/sys/fs/pipe-size-max limit when setting the capacity
	 *     of a pipe using the F_SETPIPE_SZ fcntl(2) command.
	 *   <li>Use F_SETPIPE_SZ to increase the capacity of a pipe above the limit
	 *     specified by /proc/sys/fs/pipe-max-size.
	 *   <li>Override /proc/sys/fs/mqueue/queues_max limit when creating POSIX
	 *     message queues (see mq_overview(7)).
	 *   <li>Employ prctl(2) PR_SET_MM operation.
	 *   <li>Set /proc/PID/oom_score_adj to a value lower than the value last set
	 *     by a process with CAP_SYS_RESOURCE.
	 * </ul>
	 */
	SYS_RESOURCE,
	/**
	 * <ul>
	 *   <li>Set system clock (settimeofday(2), stime(2), adjtimex(2)).
	 *   <li>Set real-time (hardware) clock.
	 * </ul>
	 */
	SYS_TIME,
	/**
	 * <ul>
	 *   <li>Use vhangup(2).
	 *   <li>Employ various privileged ioctl(2) operations on virtual terminals.
	 * </ul>
	 */
	SYS_TTY_CONFIG,
	/**
	 * Trigger something that will wake up the system (set CLOCK_REALTIME_ALARM and
	 * CLOCK_BOOTTIME_ALARM timers).
	 */
	WAKE_ALARM
}
