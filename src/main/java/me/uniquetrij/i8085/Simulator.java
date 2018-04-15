package me.uniquetrij.i8085;

import java.util.StringTokenizer;

public final class Simulator
{

    private static String brkpts;//execution breakpoints.  FORMAT: <*><address><*>
    private static boolean brake;

    private static RegisterA A;
    private static Flags F;
    private static RegisterB B;
    private static RegisterC C;
    private static RegisterD D;
    private static RegisterE E;
    private static RegisterH H;
    private static RegisterL L;
    private static RegisterM M;
    private static BCpair BC;
    private static DEpair DE;
    private static HLpair HL;
    private static ProgramStatusWord PSW;
    private static StackPointer SP;
    private static ProgramCounter PC;
    private static Memory Memory;

    public Simulator()
    {
        //initializing all class variables with default values
        A = RegisterA.RegisterA;
        F = Flags.Flags;
        B = RegisterB.RegisterB;
        C = RegisterC.RegisterC;
        D = RegisterD.RegisterD;
        E = RegisterE.RegisterE;
        H = RegisterH.RegisterH;
        L = RegisterL.RegisterL;
        M = RegisterM.RegisterM;
        PSW = ProgramStatusWord.ProgramStatusWord;
        BC = BCpair.BCpair;
        DE = DEpair.DEpair;
        HL = HLpair.HLpair;
        SP = StackPointer.StackPointer;
        PC = ProgramCounter.ProgramCounter;
        Memory = Memory.Memory;
        brkpts = "*FFFFH*";
        brake = false;
    }

    public static void simulate(String clist/*,DataEx origin*/)
    {
        //PC.set(origin);
        DataEx origin = null;
        StringTokenizer stcl;
        try
        {
            stcl = new StringTokenizer(clist, "\n");
        }
        catch (NullPointerException e)
        {
            throw new SimulatorException("Code List is null.");
        }

        while (stcl.hasMoreTokens())
        {
            StringTokenizer stc = new StringTokenizer(stcl.nextToken(), " :");
            //origin=new DataEx();
            if (stc.hasMoreTokens())
                if (origin == null)
                    PC.set(origin = new DataEx(stc.nextToken() + "H"));
                else
                    origin = new DataEx(stc.nextToken() + "H");
            while (stc.hasMoreTokens())
            {
                Memory.set(origin, new Data(stc.nextToken() + "H"));
                origin = origin.plus(1);
            }
        }
    }

    public static void startAt(DataEx origin)
    {
        PC.set(origin);
    }

    public static DataEx getProgramCounter()
    {
        return PC.data();
    }

    public static void setBrakePoint(DataEx brkpt)
    {
        StringTokenizer stk;
        try
        {
            stk = new StringTokenizer(brkpts, "*");
        }
        catch (NullPointerException e)
        {
            throw new SimulatorException("Nothing simulated yet.");
        }
        brkpts = "*";
        boolean flag = false;
        while (stk.hasMoreTokens())
        {
            String s;
            int i;
            if ((i = (s = stk.nextToken()).compareTo(brkpt.toString())) >= 0 && flag == false)
            {
                if (i > 0)
                    brkpts += brkpt + "*";
                flag = true;
            }
            brkpts += s + "*";
        }
        if (flag == false)
            brkpts += brkpt + "*";
    }

    public static void run()
    {
        int opcode;
        while (brake == false && (opcode = Fetch.data().value()) != 0x76)
            new Decode(opcode);

        if (brake == true)
        {
            brake = false;
            throw new SimulatorException(PC.data());//("Stopped Brake Point");
        }
    }

    public static void step()
    {
        new Decode(Fetch.data().value());
    }

    public static void brake()
    {
        brake = true;
    }

    public static String getBrakePoints()
    {
        return brkpts;
    }

    private static final class Execute
    {

        /**
         * Data Transfer Instructions*
         */
        public static void mov(Register Rd, Register Rs)
        {
            Rd.set(Rs.data());
        }

        public static void mvi(Register Rd, Data data)
        {
            Rd.set(data);
        }

        public static void lda(DataEx address)
        {
            A.set(Memory.data(address));
        }

        public static void ldax(SpecialRegister Rp)
        {
            Class rp = Rp.getClass();
            if (rp == PC.getClass() || rp == PSW.getClass() || rp == SP.getClass() || rp == HL.getClass())
                throw new SimulatorException("ldax", rp.getName());

            A.set(Memory.data(Rp.data()));
        }

        public static void lxi(SpecialRegister Rp, DataEx data)
        {
            Class rp = Rp.getClass();
            if (rp == PC.getClass() || rp == PSW.getClass())
                throw new SimulatorException("lxi", rp.getName());

            Rp.set(data);
        }

        public static void lhld(DataEx address)
        {
            L.set(Memory.data(address));
            address = address.plus(1);
            H.set(Memory.data(address));
        }

        public static void sta(DataEx address)
        {
            Memory.set(address, A.data());
        }

        public static void stax(SpecialRegister Rp)
        {
            Class rp = Rp.getClass();
            if (rp == PC.getClass() || rp == PSW.getClass() || rp == SP.getClass() || rp == HL.getClass())
                throw new SimulatorException("stax", rp.getName());

            Memory.set(Rp.data(), A.data());
        }

        public static void shld(DataEx address)
        {
            Memory.set(address, L.data());
            address = address.plus(1);
            Memory.set(address, H.data());
        }

        public static void xchg()
        {
            DataEx data = HL.data();
            HL.set(DE.data());
            DE.set(data);
        }

        public static void sphl()
        {
            SP.set(HL.data());
        }

        public static void xthl()
        {
            DataEx data = HL.data();
            HL.set(SP.pop());
            SP.push(data);
        }

        public static void push(SpecialRegister Rp)
        {
            Class rp = Rp.getClass();
            if (rp == PC.getClass() || rp == SP.getClass())
                throw new SimulatorException("push", rp.getName());

            SP.push(Rp.data());
        }

        public static void pop(SpecialRegister Rp)
        {
            Class rp = Rp.getClass();
            if (rp == PC.getClass() || rp == SP.getClass())
                throw new SimulatorException("pop", rp.getName());

            Rp.set(SP.pop());
        }

        public static void out(Data address)
        {
            IOunit.IOunit.set(address, A.data());
        }

        public static void in(Data address)
        {
            A.set(IOunit.IOunit.data(address));
        }

        /**
         * Flags Settings*
         */
        public static void givesAC(Data d1, Data d2, boolean complement)
        {
            if (d1.lo() + d2.lo() > 0xf)
                F.set(Flags.AC);
            else
                F.reset(Flags.AC);
        }

        public static void givesCY(Data d1, Data d2, int carry, boolean complement)
        {
            if (d1.value() + d2.value() + carry > 0xff ^ complement)
                F.set(Flags.CY);
            else
                F.reset(Flags.CY);
        }

        public static void givesP(Data d)
        {
            int count = 0;
            String p = Integer.toBinaryString(d.value());
            for (int i = 0; i < p.length(); i++)
                if (p.charAt(i) == '1')
                    count++;
            if (count % 2 == 0)
                F.set(Flags.P);
            else
                F.reset(Flags.P);
        }

        public static void givesZ(Data d)
        {
            if (d.value() == 0)
                F.set(Flags.Z);
            else
                F.reset(Flags.Z);
        }

        public static void givesS(Data d)
        {
            if (d.value() >= 0x80)
                F.set(Flags.S);
            else
                F.reset(Flags.S);
        }

        /**
         * Arithmatic Instructions*
         */
        public static void add(Register R)
        {
            Data d1 = A.data(), d2 = R.data();
            givesAC(d1, d2, false);
            givesCY(d1, d2, 0, false);
            A.set(d1 = d1.plus(d2));
            d1 = A.data();
            givesS(d1);
            givesZ(d1);
            givesP(d1);
        }

        public static void adc(Register R)
        {
            Data d1 = A.data(), d2 = R.data();
            int carry = F.get(Flags.CY) ? 1 : 0;
            givesAC(d1, d2, false);
            givesCY(d1, d2, carry, false);
            A.set(d1 = d1.plus(d2.plus(carry)));
            d1 = A.data();
            givesS(d1);
            givesZ(d1);
            givesP(d1);
        }

        public static void adi(Data data)
        {
            Data d = A.data();
            givesAC(d, data, false);
            givesCY(d, data, 0, false);
            A.set(d = d.plus(data));
            d = A.data();
            givesS(d);
            givesZ(d);
            givesP(d);
        }

        public static void aci(Data data)
        {
            Data d = A.data();
            int carry = F.get(Flags.CY) ? 1 : 0;
            givesAC(d, data, false);
            givesCY(d, data, carry, false);
            A.set(d = d.plus(data.plus(carry)));
            d = A.data();
            givesS(d);
            givesZ(d);
            givesP(d);
        }

        public static void dad(SpecialRegister Rp)
        {
            Class rp = Rp.getClass();
            if (rp == PC.getClass() || rp == PSW.getClass())
                throw new SimulatorException("dad", rp.getName());

            if (HL.data().value() + Rp.data().value() > 0xffff)
                F.set(Flags.CY);
            else
                F.reset(Flags.CY);

            HL.set(HL.data().plus(Rp.data()));
        }

        public static void sub(Register R)
        {
            Data d1 = A.data(), d2 = (new Data(0xff).minus(R.data())).plus(1);
            givesAC(d1, d2, true);
            givesCY(d1, d2, 0, true);
            A.set(d1 = d1.plus(d2));
            d1 = A.data();
            givesS(d1);
            givesZ(d1);
            givesP(d1);
        }

        public static void sbb(Register R)
        {
            Data d1 = A.data(), d2 = (new Data(0xff).minus(R.data())).plus(1);
            int carry = F.get(Flags.CY) ? 1 : 0;
            givesAC(d1, d2, true);
            givesCY(d1, d2, carry, true);
            A.set(d1 = d1.plus(d2.minus(carry)));
            d1 = A.data();
            givesS(d1);
            givesZ(d1);
            givesP(d1);
        }

        public static void sui(Data data)
        {
            Data d1 = A.data(), d2 = (new Data(0xff).minus(data)).plus(1);
            givesAC(d1, d2, true);
            givesCY(d1, d2, 0, true);
            A.set(d1 = d1.plus(d2));
            d1 = A.data();
            givesS(d1);
            givesZ(d1);
            givesP(d1);
        }

        public static void sbi(Data data)
        {
            Data d1 = A.data(), d2 = (new Data(0xff).minus(data)).plus(1);
            int carry = F.get(Flags.CY) ? 1 : 0;
            givesAC(d1, d2, true);
            givesCY(d1, d2, carry, true);
            A.set(d1 = d1.plus(d2.minus(carry)));
            d1 = A.data();
            givesS(d1);
            givesZ(d1);
            givesP(d1);
        }

        public static void inr(Register R)
        {
            Data d = R.data();
            givesAC(d, new Data(1), false);
            R.set(d = d.plus(1));
            d = R.data();
            givesS(d);
            givesZ(d);
            givesP(d);
        }

        public static void dcr(Register R)
        {
            Data d = R.data();
            givesAC(d, new Data(0xff), false);
            R.set(d = d.minus(1));
            d = R.data();
            givesS(d);
            givesZ(d);
            givesP(d);
        }

        public static void inx(SpecialRegister Rp)
        {
            Class rp = Rp.getClass();
            if (rp == PC.getClass() || rp == PSW.getClass())
                throw new SimulatorException("inx", rp.getName());

            Rp.set(Rp.data().plus(1));
        }

        public static void dcx(SpecialRegister Rp)
        {
            Class rp = Rp.getClass();
            if (rp == PC.getClass() || rp == PSW.getClass())
                throw new SimulatorException("dcx", rp.getName());

            Rp.set(Rp.data().minus(1));
        }

        public static void daa()
        {
            Data d = A.data();

            Data lo = new Data(d.lo());
            if (lo.value() > 9 || F.get(Flags.AC))
            {
                d = d.plus(0x06);
                F.set(Flags.AC);
            }
            else
                F.reset(Flags.AC);

            Data hi = new Data(d.hi());
            if (hi.value() > 9 || F.get(Flags.CY))
            {
                d = d.plus(0x60);
                F.set(Flags.CY);
            }
            else
                F.reset(Flags.CY);

            A.set(d);
        }

        /**
         * Branching Instructions*
         */
        public static void jmp(DataEx address)
        {
            PC.set(address);
        }

        public static void jc(DataEx address)
        {
            if (F.get(Flags.CY))
                PC.set(address);
        }

        public static void jnc(DataEx address)
        {
            if (!F.get(Flags.CY))
                PC.set(address);
        }

        public static void jp(DataEx address)
        {
            if (!F.get(Flags.S))
                PC.set(address);
        }

        public static void jm(DataEx address)
        {
            if (F.get(Flags.S))
                PC.set(address);
        }

        public static void jz(DataEx address)
        {
            if (F.get(Flags.Z))
                PC.set(address);
        }

        public static void jnz(DataEx address)
        {
            if (!F.get(Flags.Z))
                PC.set(address);
        }

        public static void jpe(DataEx address)
        {
            if (F.get(Flags.P))
                PC.set(address);
        }

        public static void jpo(DataEx address)
        {
            if (!F.get(Flags.P))
                PC.set(address);
        }

        public static void call(DataEx address)
        {
            SP.push(PC.data());
            PC.set(address);
        }

        public static void cc(DataEx address)
        {
            if (F.get(Flags.CY))
            {
                SP.push(PC.data());
                PC.set(address);
            }
        }

        public static void cnc(DataEx address)
        {
            if (!F.get(Flags.CY))
            {
                SP.push(PC.data());
                PC.set(address);
            }
        }

        public static void cp(DataEx address)
        {
            if (!F.get(Flags.S))
            {
                PC.set(address);
                SP.push(PC.data());
            }
        }

        public static void cm(DataEx address)
        {
            if (F.get(Flags.S))
            {
                SP.push(PC.data());
                PC.set(address);
            }
        }

        public static void cz(DataEx address)
        {
            if (F.get(Flags.Z))
            {
                SP.push(PC.data());
                PC.set(address);
            }
        }

        public static void cnz(DataEx address)
        {
            if (!F.get(Flags.Z))
            {
                SP.push(PC.data());
                PC.set(address);
            }
        }

        public static void cpe(DataEx address)
        {
            if (F.get(Flags.P))
            {
                SP.push(PC.data());
                PC.set(address);
            }
        }

        public static void cpo(DataEx address)
        {
            if (!F.get(Flags.P))
            {
                SP.push(PC.data());
                PC.set(address);
            }
        }

        public static void ret()
        {
            PC.set(SP.pop());
        }

        public static void rc()
        {
            if (F.get(Flags.CY))
                PC.set(SP.pop());
        }

        public static void rnc()
        {
            if (!F.get(Flags.CY))
                PC.set(SP.pop());
        }

        public static void rp()
        {
            if (!F.get(Flags.S))
                PC.set(SP.pop());
        }

        public static void rm()
        {
            if (F.get(Flags.S))
                PC.set(SP.pop());
        }

        public static void rz()
        {
            if (F.get(Flags.Z))
                PC.set(SP.pop());
        }

        public static void rnz()
        {
            if (!F.get(Flags.Z))
                PC.set(SP.pop());
        }

        public static void rpe()
        {
            if (F.get(Flags.P))
                PC.set(SP.pop());
        }

        public static void rpo()
        {
            if (!F.get(Flags.P))
                PC.set(SP.pop());
        }

        public static void pchl()
        {
            PC.set(HL.data());
        }

        public static void rst(double d)
        {
            call(new DataEx((int) (d * 8)));
        }

        /**
         * Logical Instructions*
         */
        public static void cmp(Register R)
        {
            /*givesCY(A.data(),R.data(),0,true);
            givesZ(new Data(A.data().minus(R.data())));*/
            if (A.data().compareTo(R.data()) == 0)
            {
                F.reset(Flags.CY);
                F.set(Flags.Z);
            }
            else if (A.data().compareTo(R.data()) > 0)
            {
                F.reset(Flags.CY);
                F.reset(Flags.Z);
            }
            else
            {
                F.set(Flags.CY);
                F.reset(Flags.Z);
            }
        }

        public static void cpi(Data d)
        {
            /*givesCY(A.data(),d,0,true);
            givesZ(new Data(A.data().minus(d)));*/
            if (A.data().compareTo(d) == 0)
            {
                F.reset(Flags.CY);
                F.set(Flags.Z);
            }
            else if (A.data().compareTo(d) > 0)
            {
                F.reset(Flags.CY);
                F.reset(Flags.Z);
            }
            else
            {
                F.set(Flags.CY);
                F.reset(Flags.Z);
            }
        }

        public static void ana(Register R)
        {
            Data d = new Data(A.data().value() & R.data().value());
            A.set(d);
            givesS(d);
            givesZ(d);
            givesP(d);
            F.set(Flags.AC);
            F.reset(Flags.CY);
        }

        public static void ani(Data d)
        {
            d = new Data(A.data().value() & d.value());
            A.set(d);
            givesS(d);
            givesZ(d);
            givesP(d);
            F.set(Flags.AC);
            F.reset(Flags.CY);
        }

        public static void xra(Register R)
        {
            Data d = new Data(A.data().value() ^ R.data().value());
            A.set(d);
            givesS(d);
            givesZ(d);
            givesP(d);
            F.reset(Flags.AC);
            F.reset(Flags.CY);
        }

        public static void xri(Data d)
        {
            d = new Data(A.data().value() ^ d.value());
            A.set(d);
            givesS(d);
            givesZ(d);
            givesP(d);
            F.reset(Flags.AC);
            F.reset(Flags.CY);
        }

        public static void ora(Register R)
        {
            Data d = new Data(A.data().value() | R.data().value());
            A.set(d);
            givesS(d);
            givesZ(d);
            givesP(d);
            F.reset(Flags.AC);
            F.reset(Flags.CY);
        }

        public static void ori(Data d)
        {
            d = new Data(A.data().value() | d.value());
            A.set(d);
            givesS(d);
            givesZ(d);
            givesP(d);
            F.reset(Flags.AC);
            F.reset(Flags.CY);
        }

        public static void rlc()
        {
            String bin = Integer.toBinaryString(A.data().value());
            bin = ("00000000".substring(bin.length()) + bin);
            char c = bin.charAt(0);
            bin = bin.substring(1) + c;
            A.set(new Data(Integer.parseInt(bin, 2)));
            if (c == '1')
                F.set(Flags.CY);
            else
                F.reset(Flags.CY);
        }

        public static void rrc()
        {
            String bin = Integer.toBinaryString(A.data().value());
            bin = ("00000000".substring(bin.length()) + bin);
            char c = bin.charAt(7);
            bin = c + bin.substring(0, 7);
            A.set(new Data(Integer.parseInt(bin, 2)));
            if (c == '1')
                F.set(Flags.CY);
            else
                F.reset(Flags.CY);
        }

        public static void ral()
        {
            String bin = Integer.toBinaryString(A.data().value());
            bin = ("00000000".substring(bin.length()) + bin);
            char c = F.get(Flags.CY) ? '1' : '0';
            bin += c;
            c = bin.charAt(0);
            bin = bin.substring(1);
            A.set(new Data(Integer.parseInt(bin, 2)));
            if (c == '1')
                F.set(Flags.CY);
            else
                F.reset(Flags.CY);
        }

        public static void rar()
        {
            String bin = Integer.toBinaryString(A.data().value());
            bin = ("00000000".substring(bin.length()) + bin);
            char c = F.get(Flags.CY) ? '1' : '0';
            bin = c + bin;
            c = bin.charAt(8);
            bin = bin.substring(0, 8);
            A.set(new Data(Integer.parseInt(bin, 2)));
            if (c == '1')
                F.set(Flags.CY);
            else
                F.reset(Flags.CY);
        }

        public static void cma()
        {
            A.set(new Data(A.data().value() ^ 0xff));
        }

        public static void cmc()
        {
            if (F.get(Flags.CY))
                F.reset(Flags.CY);
            else
                F.set(Flags.CY);
        }

        public static void stc()
        {
            F.set(Flags.CY);
        }

        /**
         * Control Instructions*
         */
        public static void nop()
        {
            return;
        }

        public static void hlt()
        {
            return;
        }

        public static void di()
        {
            return;
        }

        public static void ei()
        {
            return;
        }

        public static void rim()
        {
            return;
        }

        public static void sim()
        {
            return;
        }
    }

    private static final class Decode
    {

        public Decode(int opcode)
        {

            switch (opcode)
            {
                case 0x00:
                    Execute.nop();
                    break;

                case 0x01:
                    Execute.lxi(BC, new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0x02:
                    Execute.stax(BC);
                    break;

                case 0x03:
                    Execute.inx(BC);
                    break;

                case 0x04:
                    Execute.inr(B);
                    break;

                case 0x05:
                    Execute.dcr(B);
                    break;

                case 0x06:
                    Execute.mvi(B, Fetch.data());
                    break;

                case 0x07:
                    Execute.rlc();
                    break;

                //case 0x08:  Execute.break;
                case 0x09:
                    Execute.dad(BC);
                    break;

                case 0x0a:
                    Execute.ldax(BC);
                    break;

                case 0x0b:
                    Execute.dcx(BC);
                    break;

                case 0x0c:
                    Execute.inr(C);
                    break;

                case 0x0d:
                    Execute.dcr(C);
                    break;

                case 0x0e:
                    Execute.mvi(C, Fetch.data());
                    break;

                case 0x0f:
                    Execute.rrc();
                    break;

                //case 0x10:  Execute.break;
                case 0x11:
                    Execute.lxi(DE, new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0x12:
                    Execute.stax(DE);
                    break;

                case 0x13:
                    Execute.inx(DE);
                    break;

                case 0x14:
                    Execute.inr(D);
                    break;

                case 0x15:
                    Execute.dcr(D);
                    break;

                case 0x16:
                    Execute.mvi(D, Fetch.data());
                    break;

                case 0x17:
                    Execute.ral();
                    break;

                //case 0x18:  Execute.break;
                case 0x19:
                    Execute.dad(DE);
                    break;

                case 0x1a:
                    Execute.ldax(DE);
                    break;

                case 0x1b:
                    Execute.dcx(DE);
                    break;

                case 0x1c:
                    Execute.inr(E);
                    break;

                case 0x1d:
                    Execute.dcr(E);
                    break;

                case 0x1e:
                    Execute.mvi(E, Fetch.data());
                    break;

                case 0x1f:
                    Execute.rar();
                    break;

                case 0x20:
                    Execute.rim();
                    break;

                case 0x21:
                    Execute.lxi(HL, new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0x22:
                    Execute.shld(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0x23:
                    Execute.inx(HL);
                    break;

                case 0x24:
                    Execute.inr(H);
                    break;

                case 0x25:
                    Execute.dcr(H);
                    break;

                case 0x26:
                    Execute.mvi(H, Fetch.data());
                    break;

                case 0x27:
                    Execute.daa();
                    break;

                //case 0x28:  Execute.break;
                case 0x29:
                    Execute.dad(HL);
                    break;

                case 0x2a:
                    Execute.lhld(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0x2b:
                    Execute.dcx(HL);
                    break;

                case 0x2c:
                    Execute.inr(L);
                    break;

                case 0x2d:
                    Execute.dcr(L);
                    break;

                case 0x2e:
                    Execute.mvi(L, Fetch.data());
                    break;

                case 0x2f:
                    Execute.cma();
                    break;

                case 0x30:
                    Execute.sim();
                    break;

                case 0x31:
                    Execute.lxi(SP, new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0x32:
                    Execute.sta(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0x33:
                    Execute.inx(PC);
                    break;

                case 0x34:
                    Execute.inr(M);
                    break;

                case 0x35:
                    Execute.dcr(M);
                    break;

                case 0x36:
                    Execute.mvi(M, Fetch.data());
                    break;

                case 0x37:
                    Execute.stc();
                    break;

                //case 0x38:  Execute.break;
                case 0x39:
                    Execute.dad(PC);
                    break;

                case 0x3a:
                    Execute.lda(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0x3b:
                    Execute.dcx(PC);
                    break;

                case 0x3c:
                    Execute.inr(A);
                    break;

                case 0x3d:
                    Execute.dcr(A);
                    break;

                case 0x3e:
                    Execute.mvi(A, Fetch.data());
                    break;

                case 0x3f:
                    Execute.cmc();
                    break;

                case 0x40:
                    Execute.mov(B, B);
                    break;

                case 0x41:
                    Execute.mov(B, C);
                    break;

                case 0x42:
                    Execute.mov(B, D);
                    break;

                case 0x43:
                    Execute.mov(B, E);
                    break;

                case 0x44:
                    Execute.mov(B, H);
                    break;

                case 0x45:
                    Execute.mov(B, L);
                    break;

                case 0x46:
                    Execute.mov(B, M);
                    break;

                case 0x47:
                    Execute.mov(B, A);
                    break;

                case 0x48:
                    Execute.mov(C, B);
                    break;

                case 0x49:
                    Execute.mov(C, C);
                    break;

                case 0x4a:
                    Execute.mov(C, D);
                    break;

                case 0x4b:
                    Execute.mov(C, E);
                    break;

                case 0x4c:
                    Execute.mov(C, H);
                    break;

                case 0x4d:
                    Execute.mov(C, L);
                    break;

                case 0x4e:
                    Execute.mov(C, M);
                    break;

                case 0x4f:
                    Execute.mov(C, A);
                    break;

                case 0x50:
                    Execute.mov(D, B);
                    break;

                case 0X51:
                    Execute.mov(D, C);
                    break;

                case 0x52:
                    Execute.mov(D, D);
                    break;

                case 0x53:
                    Execute.mov(D, E);
                    break;

                case 0x54:
                    Execute.mov(D, H);
                    break;

                case 0x55:
                    Execute.mov(D, L);
                    break;

                case 0x56:
                    Execute.mov(D, M);
                    break;

                case 0x57:
                    Execute.mov(D, A);
                    break;

                case 0x58:
                    Execute.mov(E, B);
                    break;

                case 0x59:
                    Execute.mov(E, C);
                    break;

                case 0x5a:
                    Execute.mov(E, D);
                    break;

                case 0x5b:
                    Execute.mov(E, E);
                    break;

                case 0x5c:
                    Execute.mov(E, H);
                    break;

                case 0x5d:
                    Execute.mov(E, L);
                    break;

                case 0x5e:
                    Execute.mov(E, M);
                    break;

                case 0x5f:
                    Execute.mov(E, A);
                    break;

                case 0x60:
                    Execute.mov(H, B);
                    break;

                case 0X61:
                    Execute.mov(H, C);
                    break;

                case 0x62:
                    Execute.mov(H, D);
                    break;

                case 0x63:
                    Execute.mov(H, E);
                    break;

                case 0x64:
                    Execute.mov(H, H);
                    break;

                case 0x65:
                    Execute.mov(H, L);
                    break;

                case 0x66:
                    Execute.mov(H, M);
                    break;

                case 0x67:
                    Execute.mov(H, A);
                    break;

                case 0x68:
                    Execute.mov(L, B);
                    break;

                case 0x69:
                    Execute.mov(L, C);
                    break;

                case 0x6a:
                    Execute.mov(L, D);
                    break;

                case 0x6b:
                    Execute.mov(L, E);
                    break;

                case 0x6c:
                    Execute.mov(L, H);
                    break;

                case 0x6d:
                    Execute.mov(L, L);
                    break;

                case 0x6e:
                    Execute.mov(L, M);
                    break;

                case 0x6f:
                    Execute.mov(L, A);
                    break;

                case 0x70:
                    Execute.mov(M, B);
                    break;

                case 0X71:
                    Execute.mov(M, C);
                    break;

                case 0x72:
                    Execute.mov(M, D);
                    break;

                case 0x73:
                    Execute.mov(M, E);
                    break;

                case 0x74:
                    Execute.mov(M, H);
                    break;

                case 0x75:
                    Execute.mov(M, L);
                    break;

                case 0x76:
                    Execute.hlt();
                    break;

                case 0x77:
                    Execute.mov(M, A);
                    break;

                case 0x78:
                    Execute.mov(A, B);
                    break;

                case 0x79:
                    Execute.mov(A, C);
                    break;

                case 0x7a:
                    Execute.mov(A, D);
                    break;

                case 0x7b:
                    Execute.mov(A, E);
                    break;

                case 0x7c:
                    Execute.mov(A, H);
                    break;

                case 0x7d:
                    Execute.mov(A, L);
                    break;

                case 0x7e:
                    Execute.mov(A, M);
                    break;

                case 0x7f:
                    Execute.mov(A, A);
                    break;

                case 0x80:
                    Execute.add(B);
                    break;

                case 0x81:
                    Execute.add(C);
                    break;

                case 0x82:
                    Execute.add(D);
                    break;

                case 0x83:
                    Execute.add(E);
                    break;

                case 0x84:
                    Execute.add(H);
                    break;

                case 0x85:
                    Execute.add(L);
                    break;

                case 0x86:
                    Execute.add(M);
                    break;

                case 0x87:
                    Execute.add(A);
                    break;

                case 0x88:
                    Execute.adc(B);
                    break;

                case 0x89:
                    Execute.adc(C);
                    break;

                case 0x8a:
                    Execute.adc(D);
                    break;

                case 0x8b:
                    Execute.adc(E);
                    break;

                case 0x8c:
                    Execute.adc(H);
                    break;

                case 0x8d:
                    Execute.adc(L);
                    break;

                case 0x8e:
                    Execute.adc(M);
                    break;

                case 0x8f:
                    Execute.adc(A);
                    break;

                case 0x90:
                    Execute.sub(B);
                    break;

                case 0x91:
                    Execute.sub(C);
                    break;

                case 0x92:
                    Execute.sub(D);
                    break;

                case 0x93:
                    Execute.sub(E);
                    break;

                case 0x94:
                    Execute.sub(H);
                    break;

                case 0x95:
                    Execute.sub(L);
                    break;

                case 0x96:
                    Execute.sub(M);
                    break;

                case 0x97:
                    Execute.sub(A);
                    break;

                case 0x98:
                    Execute.sbb(B);
                    break;

                case 0x99:
                    Execute.sbb(C);
                    break;

                case 0x9a:
                    Execute.sbb(D);
                    break;

                case 0x9b:
                    Execute.sbb(E);
                    break;

                case 0x9c:
                    Execute.sbb(H);
                    break;

                case 0x9d:
                    Execute.sbb(L);
                    break;

                case 0x9e:
                    Execute.sbb(M);
                    break;

                case 0x9f:
                    Execute.sbb(A);
                    break;

                case 0xa0:
                    Execute.ana(B);
                    break;

                case 0xa1:
                    Execute.ana(C);
                    break;

                case 0xa2:
                    Execute.ana(D);
                    break;

                case 0xa3:
                    Execute.ana(E);
                    break;

                case 0xa4:
                    Execute.ana(H);
                    break;

                case 0xa5:
                    Execute.ana(L);
                    break;

                case 0xa6:
                    Execute.ana(M);
                    break;

                case 0xa7:
                    Execute.ana(A);
                    break;

                case 0xa8:
                    Execute.xra(B);
                    break;

                case 0xa9:
                    Execute.xra(C);
                    break;

                case 0xaa:
                    Execute.xra(D);
                    break;

                case 0xab:
                    Execute.xra(E);
                    break;

                case 0xac:
                    Execute.xra(H);
                    break;

                case 0xad:
                    Execute.xra(L);
                    break;

                case 0xae:
                    Execute.xra(M);
                    break;

                case 0xaf:
                    Execute.xra(A);
                    break;

                case 0xb0:
                    Execute.ora(B);
                    break;

                case 0xb1:
                    Execute.ora(C);
                    break;

                case 0xb2:
                    Execute.ora(D);
                    break;

                case 0xb3:
                    Execute.ora(E);
                    break;

                case 0xb4:
                    Execute.ora(H);
                    break;

                case 0xb5:
                    Execute.ora(L);
                    break;

                case 0xb6:
                    Execute.ora(M);
                    break;

                case 0xb7:
                    Execute.ora(A);
                    break;

                case 0xb8:
                    Execute.cmp(B);
                    break;

                case 0xb9:
                    Execute.cmp(C);
                    break;

                case 0xba:
                    Execute.cmp(D);
                    break;

                case 0xbb:
                    Execute.cmp(E);
                    break;

                case 0xbc:
                    Execute.cmp(H);
                    break;

                case 0xbd:
                    Execute.cmp(L);
                    break;

                case 0xbe:
                    Execute.cmp(M);
                    break;

                case 0xbf:
                    Execute.cmp(A);
                    break;

                case 0xc0:
                    Execute.rnz();
                    break;

                case 0xc1:
                    Execute.pop(BC);
                    break;

                case 0xc2:
                    Execute.jnz(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xc3:
                    Execute.jmp(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xc4:
                    Execute.cnz(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xc5:
                    Execute.push(BC);
                    break;

                case 0xc6:
                    Execute.adi(Fetch.data());
                    break;

                case 0xc7:
                    Execute.rst(0);
                    break;

                case 0xc8:
                    Execute.rz();
                    break;

                case 0xc9:
                    Execute.ret();
                    break;

                case 0xca:
                    Execute.jz(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                //case 0xcb:  Execute.break;
                case 0xcc:
                    Execute.cz(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xcd:
                    Execute.call(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xce:
                    Execute.aci(Fetch.data());
                    break;

                case 0xcf:
                    Execute.rst(1);
                    break;

                case 0xd0:
                    Execute.rnc();
                    break;

                case 0xd1:
                    Execute.pop(DE);
                    break;

                case 0xd2:
                    Execute.jnc(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xd3:
                    Execute.out(Fetch.data());
                    break;

                case 0xd4:
                    Execute.cnc(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xd5:
                    Execute.push(DE);
                    break;

                case 0xd6:
                    Execute.sui(Fetch.data());
                    break;

                case 0xd7:
                    Execute.rst(2);
                    break;

                case 0xd8:
                    Execute.rc();
                    break;

                //case 0xd9:  Execute.break;
                case 0xda:
                    Execute.jc(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xdb:
                    Execute.in(Fetch.data());
                    break;

                case 0xdc:
                    Execute.cc(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                //case 0xdd:  Execute.break;
                case 0xde:
                    Execute.sbi(Fetch.data());
                    break;

                case 0xdf:
                    Execute.rst(3);
                    break;

                case 0xe0:
                    Execute.rpo();
                    break;

                case 0xe1:
                    Execute.pop(HL);
                    break;

                case 0xe2:
                    Execute.jpo(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xe3:
                    Execute.xthl();
                    break;

                case 0xe4:
                    Execute.cpo(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xe5:
                    Execute.push(HL);
                    break;

                case 0xe6:
                    Execute.ani(Fetch.data());
                    break;

                case 0xe7:
                    Execute.rst(4);
                    break;

                case 0xe8:
                    Execute.rpe();
                    break;

                case 0xe9:
                    Execute.pchl();
                    break;

                case 0xea:
                    Execute.jpe(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xeb:
                    Execute.xchg();
                    break;

                case 0xec:
                    Execute.cpe(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                //case 0xed:  Execute.break;
                case 0xee:
                    Execute.xri(Fetch.data());
                    break;

                case 0xef:
                    Execute.rst(5);
                    break;

                case 0xf0:
                    Execute.rp();
                    break;

                case 0xf1:
                    Execute.pop(PSW);
                    break;

                case 0xf2:
                    Execute.jp(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xf3:
                    Execute.di();
                    break;

                case 0xf4:
                    Execute.cp(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xf5:
                    Execute.push(PSW);
                    break;

                case 0xf6:
                    Execute.ori(Fetch.data());
                    break;

                case 0xf7:
                    Execute.rst(6);
                    break;

                case 0xf8:
                    Execute.rm();
                    break;

                case 0xf9:
                    Execute.sphl();
                    break;

                case 0xfa:
                    Execute.jm(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                case 0xfb:
                    Execute.ei();
                    break;

                case 0xfc:
                    Execute.cm(new DataEx(Fetch.data(), Fetch.data()));
                    break;

                //case 0xfd:  Execute.break;
                case 0xfe:
                    Execute.cpi(Fetch.data());
                    break;

                case 0xff:
                    Execute.rst(7);
                    break;

                default:
                    throw new SimulatorException(opcode, PC.data().minus(1));
            }
        }
    }

    private static final class Fetch
    {

        public static Data data()
        {
            if (brkpts.indexOf(PC.data().toString()) >= 0)
                brake();

            return PC.obtain();

        }
    }
}
