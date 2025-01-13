import { Button } from 'primereact/button';

export default function About() {
	return (
		<>
			<div className="about mx-3 my-5">
				<a href='https://github.com/rsp243'>
					<Button label="Github rsp243" icon="pi pi-github" />
				</a>
				<a href='https://github.com/Lannee'>
					<Button label="Github Lannee" icon="pi pi-github" />
				</a>
			</div>
			<div className="about mx-3 my-5">
				<a className="mx-3" href='https://t.me/rsp243'>
					<Button label="Telegram rsp243" icon="pi pi-telegram" />
				</a>
				<a className="mx-3" href='https://t.me/JonneTerre'>
					<Button label="Telegram Lanee" icon="pi pi-telegram" />
				</a>
			</div>
		</>
	);
}
