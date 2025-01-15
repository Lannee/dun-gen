import { Button } from 'primereact/button';

export default function About() {
	return (
		<>
			<div className="about mx-5 my-5">
				Dun'Gen (Dungeons & Dragons Hero Generator) was made by ITMO Software Engineering and Computer Technology faculty students: <br></br>
				<h3>Reshetov Semen, Bulko Egor P3306</h3>
			</div>
			<div className="about mx-3 my-5">
				<a className="mx-3" href='https://github.com/rsp243'>
					<Button label="Reshetov's Github" icon="pi pi-github" />
				</a>
				<a className="mx-3" href='https://github.com/Lannee'>
					<Button label="Bulko's Github" icon="pi pi-github" />
				</a>
			</div>
			<div className="about mx-3 my-5">
				<a className="mx-3" href='https://t.me/rsp243'>
					<Button label="Reshetov's Telegram" icon="pi pi-telegram" />
				</a>
				<a className="mx-3" href='https://t.me/JonneTerre'>
					<Button label="Bulko's Telegram" icon="pi pi-telegram" />
				</a>
			</div>
		</>
	);
}
